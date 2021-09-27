package se.liu.ida.tdp024.account.logic.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;

public class TransactionLogicFacadeTest {
    
    //--- Unit under test ---//
    public TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    public AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
    public AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacade);
    public TransactionLogicFacade transactionLogicFacade = new TransactionLogicFacadeImpl(transactionEntityFacade);
    public StorageFacade storageFacade = new StorageFacadeDB();
    
    @After
    public void tearDown() {
        if (storageFacade != null)
            storageFacade.emptyStorage();
    }

    @Test
    public void testGetTransactions() {
        String personKey = "1";
        accountLogicFacade.create("CHECK", personKey, "2");
        Account acc = accountLogicFacade.findPerson(personKey).get(0);
        long accountID = acc.getId();
        List<Transaction> transactions = transactionLogicFacade.getTransactions(accountID);
        assert(transactions.size() == 0);

        String res = transactionEntityFacade.create("CREDIT", 200, "2013-06-30 14:49:32", "OK", acc);
        assert(res.equals("OK"));
        res = transactionEntityFacade.create("DEBIT", 250, "2013-06-30 15:49:32", "FAILED", acc);
        assert(res.equals("OK"));
        transactions = transactionLogicFacade.getTransactions(accountID);
        assert(transactions.size() == 2);
    }
}
    