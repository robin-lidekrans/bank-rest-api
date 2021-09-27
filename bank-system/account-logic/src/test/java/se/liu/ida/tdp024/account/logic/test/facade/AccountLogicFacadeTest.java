package se.liu.ida.tdp024.account.logic.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

public class AccountLogicFacadeTest {
    
    //--- Unit under test ---//
    public TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    public AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
    public AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacade);
    public StorageFacade storageFacade = new StorageFacadeDB();
    
    @After
    public void tearDown() {
        if (storageFacade != null)
        storageFacade.emptyStorage();
    }
    
    
    
    @Test
    public void testCreate() {
        String res;
        
        // Valid account, person, bank
        res = accountLogicFacade.create("CHECK", "1", "NORDEA");
        assert(res == "OK");
        
        // Valid account, bank, invalid person
        res = accountLogicFacade.create("CHECK", "1337", "NORDEA");
        assert(res == "FAILED");
        
        // Valid account, person, invalid bank
        res = accountLogicFacade.create("CHECK", "1", "DANSKEBANK");
        assert(res == "FAILED");
        
        // Invalid account, valid person, bank
        res = accountLogicFacade.create("CREDITCARD", "1", "NORDEA");
        assert(res == "FAILED");
        
        res = accountLogicFacade.create("", "", "");
        assert(res == "FAILED");
    }

    @Test
    public void testCredit() {
        accountLogicFacade.create("CHECK", "2", "3");
        long accountID = accountLogicFacade.findPerson("2").get(0).getId();
        accountLogicFacade.creditAccount(accountID, 200);
        assert(accountEntityFacade.getAccount(accountID).getHoldings() == 200);
    }
    
    @Test
    public void testDebit() {
        accountLogicFacade.create("CHECK", "1", "NORDEA");
        List<Account> foundAccounts = accountLogicFacade.findPerson("1");
        long accountID = foundAccounts.get(0).getId();

        accountLogicFacade.creditAccount(accountID, 200);
        String res = accountLogicFacade.debitAccount(accountID, 100);
        assert(accountEntityFacade.getAccount(accountID).getHoldings() == 100);
        assert(res.equals("OK"));

        res = accountLogicFacade.debitAccount(accountID, 999);
        assert(accountEntityFacade.getAccount(accountID).getHoldings() == 100);
        assert(res.equals("FAILED"));
    }
}
