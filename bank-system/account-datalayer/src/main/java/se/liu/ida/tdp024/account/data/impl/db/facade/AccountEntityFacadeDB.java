package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.util.kafka.KafkaTopic;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtil;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtilImpl;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {

    private TransactionEntityFacade transactionEntityFacade;
    private final KafkaUtil kafkaUtil = new KafkaUtilImpl();

    private final static String CREATE_INIT = "Attempting to write new account to database";
    private final static String CREATE_SUCCESS = "Writing account to database successful.";
    private final static String CREATE_ERROR = "Error when writing account to database.";

    private final static String GET_LIST_INIT = "Querying database for accounts.";
    private final static String GET_LIST_SUCCESS = "Account database query successful, returning results.";
    private final static String GET_INIT = "Querying account database for account.";
    private final static String GET_SUCCESS = "Account database query successful, returning result.";
    private final static String GET_ERROR = "Error when querying account database.";

    private final static String CREDIT_INIT = "Attempting to credit account.";
    private final static String CREDIT_SUCCESS = "Crediting account successful.";
    private final static String CREDIT_ERROR = "Error crediting account.";
    private final static String CREDIT_ERROR_INVALID_ACCOUNT = "Error crediting account: Invalid account";

    private final static String DEBIT_INIT = "Attempting to debit account.";
    private final static String DEBIT_SUCCESS = "Debiting account successful.";
    private final static String DEBIT_TRANSACTION_SUCCESS = "Account debit transaction completed.";
    private final static String DEBIT_ERROR = "Error debititing account.";
    private final static String DEBIT_ERROR_INVALID_ACCOUNT = "Error debiting account: Invalid account.";
    private final static String DEBIT_ERROR_INSUFFICIENT_FUNDS = "Error debiting account: Insufficient funds.";
    
    public AccountEntityFacadeDB(TransactionEntityFacade transactionEntityFacade) {
        this.transactionEntityFacade = transactionEntityFacade;
    }
    
    @Override
    public String create(String accountType, String personKey, String bankKey) {
        EntityManager em = EMF.getEntityManager();
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREATE_INIT);
        try {
            em.getTransaction().begin();
            Account account = new AccountDB();
            account.setAccountType(accountType);
            account.setPersonKey(personKey);
            account.setBankKey(bankKey);
            account.setHoldings(0);
            em.persist(account);
            em.getTransaction().commit();
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREATE_SUCCESS);
            return "OK";
        } catch (Exception e) {
            em.getTransaction().rollback();
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREATE_ERROR);
            return "FAILED";
        } finally {
            em.close();
        }
    }

    @Override
    public List<Account> get(String personKey) {
        EntityManager em = EMF.getEntityManager();
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_LIST_INIT);
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT acc FROM AccountDB acc WHERE acc.personKey = :personKey", Account.class);
            query.setParameter("personKey", personKey);
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_LIST_SUCCESS);
            return query.getResultList();
        } catch (Exception e) {
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_ERROR);
            return new ArrayList<Account>();
        } finally {
            em.close();
        }
    }

    @Override
    public Account getAccount(long accountID) {
        EntityManager em = EMF.getEntityManager();
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_INIT);
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT acc FROM AccountDB acc WHERE acc.id = :accountID", Account.class);
            query.setParameter("accountID", accountID);
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_SUCCESS);
            return query.getSingleResult();
        } catch (Exception e) {
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_ERROR);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public String debitAccount(Long accountID, int amount) {
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_INIT);
        EntityManager em = EMF.getEntityManager();
        String status;

        em.getTransaction().begin();
        Account account = em.find(AccountDB.class, accountID, LockModeType.PESSIMISTIC_WRITE);
        String timeStamp = LocalDateTime.now().toString();
        // Find returns null if no account was found
        if (account == null) {
            em.getTransaction().rollback();
            em.close();
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_ERROR_INVALID_ACCOUNT);

            return "FAILED";
        }

        int newBalance = account.getHoldings() - amount;
        if (newBalance < 0)  {
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_ERROR_INSUFFICIENT_FUNDS);
            status = "FAILED";
        } 
        else {
            status = "OK";
            account.setHoldings(newBalance);
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_SUCCESS);
        }
        try {
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_ERROR);
            return "FAILED";
        }
        em.close();
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, DEBIT_TRANSACTION_SUCCESS);
        transactionEntityFacade.create("DEBIT", amount, timeStamp, status, account);
        return status;
    }

    @Override
    public String creditAccount(Long accountID, int amount) {
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREDIT_INIT);
        EntityManager em = EMF.getEntityManager();
        String status;

        em.getTransaction().begin();
        Account account = em.find(AccountDB.class, accountID, LockModeType.PESSIMISTIC_WRITE);
        String timeStamp = LocalDateTime.now().toString();
        // Find returns null if no account was found
        if (account == null) {
            em.getTransaction().rollback();
            em.close();
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREDIT_ERROR_INVALID_ACCOUNT);
            return "FAILED";
        }
        int newBalance = account.getHoldings() + amount;
        account.setHoldings(newBalance);
        status = "OK";
        try {
            em.getTransaction().commit();
        } catch (Exception e) {
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREDIT_ERROR);
            em.getTransaction().rollback();
            em.close();
            return "FAILED";
        } 
        em.close();
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREDIT_SUCCESS);
        transactionEntityFacade.create("CREDIT", amount, timeStamp, status, account);
        return status;
    }
}
