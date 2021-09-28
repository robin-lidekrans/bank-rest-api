package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {

    private TransactionEntityFacade transactionEntityFacade;

    public AccountEntityFacadeDB(TransactionEntityFacade transactionEntityFacade) {
        this.transactionEntityFacade = transactionEntityFacade;
    }
    
    @Override
    public String create(String accountType, String personKey, String bankKey) {
        EntityManager em = EMF.getEntityManager();
        try {
            em.getTransaction().begin();
            Account account = new AccountDB();
            account.setAccountType(accountType);
            account.setPersonKey(personKey);
            account.setBankKey(bankKey);
            account.setHoldings(0);
            em.persist(account);
            em.getTransaction().commit();
            return "OK";
        } catch (Exception e) {
            em.getTransaction().rollback();
            return "FAILED";
        } finally {
            em.close();
        }
    }

    @Override
    public List<Account> get(String personKey) {
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT acc FROM AccountDB acc WHERE acc.personKey = :personKey", Account.class);
            query.setParameter("personKey", personKey);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Account getAccount(long accountID) {
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT acc FROM AccountDB acc WHERE acc.id = :accountID", Account.class);
            query.setParameter("accountID", accountID);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public String debitAccount(Long accountID, int amount) {
        EntityManager em = EMF.getEntityManager();
        String status;

        em.getTransaction().begin();
        Account account = em.find(AccountDB.class, accountID, LockModeType.PESSIMISTIC_WRITE);
        String timeStamp = LocalDateTime.now().toString();
        // Find returns null if no account was found
        if (account == null) {
            em.getTransaction().commit();
            return "FAILED";
        }

        int newBalance = account.getHoldings() - amount;
        if (newBalance < 0)  {
            status = "FAILED";
        } 
        else {
            status = "OK";
            account.setHoldings(newBalance);
        }
        try {
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            return "FAILED";
        }
        em.close();
        transactionEntityFacade.create("DEBIT", amount, timeStamp, status, account);
        return status;
    }

    @Override
    public String creditAccount(Long accountID, int amount) {
        EntityManager em = EMF.getEntityManager();
        String status;

        em.getTransaction().begin();
        Account account = em.find(AccountDB.class, accountID, LockModeType.PESSIMISTIC_WRITE);
        String timeStamp = LocalDateTime.now().toString();
        // Find returns null if no account was found
        if (account == null) {
            em.close();
            return "FAILED";
        }
        int newBalance = account.getHoldings() + amount;
        account.setHoldings(newBalance);
        status = "OK";
        try {
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.close();
            return "FAILED";
        } 
        em.close();
        transactionEntityFacade.create("CREDIT", amount, timeStamp, status, account);
        return status;
    }
}
