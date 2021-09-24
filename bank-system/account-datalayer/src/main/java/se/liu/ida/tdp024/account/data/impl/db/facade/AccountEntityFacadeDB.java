package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {

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
}
