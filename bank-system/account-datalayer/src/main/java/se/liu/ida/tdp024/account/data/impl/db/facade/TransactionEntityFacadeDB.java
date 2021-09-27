package se.liu.ida.tdp024.account.data.impl.db.facade;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {

    @Override
    public String create(String transactionType, int transactionAmount, String timeStamp, String status, Account account) {
        EntityManager em = EMF.getEntityManager();
        try {
            em.getTransaction().begin();
            Transaction transaction = new TransactionDB();
            transaction.setTransactionType(transactionType);
            transaction.setAmount(transactionAmount);
            transaction.setTimeStamp(timeStamp);
            transaction.setStatus(status);
            transaction.setAccount(account);
            em.persist(transaction);
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
    public List<Transaction> get(Long accountId) {
        EntityManager em = EMF.getEntityManager();
        try {
            TypedQuery<Transaction> query = em.createQuery(
                    "SELECT trans FROM TransactionDB trans WHERE trans.account.id = :accountId", Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
}
