package se.liu.ida.tdp024.account.data.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;

public class AccountEntityFacadeTest {
    
    //---- Unit under test ----//
    private TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
    private StorageFacade storageFacade = new StorageFacadeDB();
    
    String accountType = "CHECK";
    String personKey = "5";
    String bankKey = "1";
    int holdings = 999;

    @Before
    public void setup() {
        transactionEntityFacade = new TransactionEntityFacadeDB();
        accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
        storageFacade = new StorageFacadeDB();
    }

    @After
    public void tearDown() {
       storageFacade.emptyStorage();
    }
    
    @Test
    public void testCreate() {
        String validAcc = accountEntityFacade.create(accountType, personKey, bankKey);
        assert(validAcc.equals("OK"));
    }

    @Test
    public void testGet() {
        List<Account> res = accountEntityFacade.get(personKey);
        assert(res.size() == 0);

        accountEntityFacade.create(accountType, personKey, bankKey);
        res = accountEntityFacade.get(personKey);
        assert(res.size() == 1);

        accountEntityFacade.create(accountType + 1, personKey, bankKey + 1);
        accountEntityFacade.create(accountType + 2, personKey, bankKey + 2);
        res = accountEntityFacade.get(personKey);
        assert(res.size() == 3);

        accountEntityFacade.create(accountType + 2, personKey + 1, bankKey + 2);
        res = accountEntityFacade.get(personKey);
        assert(res.size() == 3);

        assert(accountEntityFacade.get(personKey).get(0).getId() == res.get(0).getId());
    }

    @Test
    public void testSettersAndGetters() {
        AccountDB acc = new AccountDB();
        long accID = acc.getId();
        assert(acc.getAccountType() == null);
        assert(acc.getBankKey() == null);
        assert(acc.getPersonKey() == null);
        assert(acc.getHoldings() == 0);

        acc.setAccountType(accountType);
        acc.setBankKey(bankKey);
        acc.setHoldings(holdings);
        acc.setPersonKey(personKey);
        assert(acc.getAccountType().equals(accountType));
        assert(acc.getBankKey().equals(bankKey));
        assert(acc.getPersonKey().equals(personKey));
        assert(acc.getHoldings() == holdings);
        assert(accID == acc.getId());
    }

    @Test
    public void testCreditDebitAccount() {
        accountEntityFacade.create(accountType, personKey, bankKey);
        long accID = accountEntityFacade.get(personKey).get(0).getId();

        String res = accountEntityFacade.debitAccount(accID, 50);
        assert(res == "FAILED");
        assert(accountEntityFacade.getAccount(accID).getHoldings() == 0);

        res = accountEntityFacade.creditAccount(accID, 50);
        assert(res == "OK");
        assert(accountEntityFacade.getAccount(accID).getHoldings() == 50);

        res = accountEntityFacade.debitAccount(accID, 25);
        assert(res == "OK");
        assert(accountEntityFacade.getAccount(accID).getHoldings() == 25);

        res = accountEntityFacade.debitAccount(accID + 1, 25);
        assert(res == "FAILED");

        res = accountEntityFacade.creditAccount(accID + 1, 25);
        assert(res == "FAILED");
    }
}