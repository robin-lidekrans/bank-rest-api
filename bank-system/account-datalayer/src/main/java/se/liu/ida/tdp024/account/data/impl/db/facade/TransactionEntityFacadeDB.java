package se.liu.ida.tdp024.account.data.impl.db.facade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;
import se.liu.ida.tdp024.account.util.kafka.KafkaTopic;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtil;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtilImpl;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {

    private final KafkaUtil kafkaUtil = new KafkaUtilImpl();

    private final static String CREATE_INIT = "Attempting to write new transaction to database.";
    private final static String CREATE_SUCCESS = "Writing transaction to database successful.";
    private final static String CREATE_ERROR = "Error when writing transaction to database.";

    private final static String GET_INIT = "Querying database for transactions.";
    private final static String GET_SUCCESS = "Transaction database query successful, returning results.";
    private final static String GET_ERROR = "Error when querying transaction database.";

    @Override
    public String create(String type, int amount, String created, String status, Account account) {
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, CREATE_INIT);
        EntityManager em = EMF.getEntityManager();
        try {
            em.getTransaction().begin();
            Transaction transaction = new TransactionDB();
            transaction.setType(type);
            transaction.setAmount(amount);
            transaction.setCreated(created);
            transaction.setStatus(status);
            transaction.setAccount(account);
            em.persist(transaction);
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
    public List<Transaction> get(Long accountId) {
        kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_INIT);
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                    "SELECT trans FROM TransactionDB trans WHERE trans.account.id = :accountId", Transaction.class);
            query.setParameter("accountId", accountId);
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_SUCCESS);
            return query.getResultList();
        } catch (Exception e) {
            kafkaUtil.publishMessage(KafkaTopic.TRANSACTION, GET_ERROR);
            return new ArrayList<Transaction>();
        } finally {
            em.close();
        }
    }
    
}
