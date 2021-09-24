package se.liu.ida.tdp024.account.data.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;

public class AccountEntityFacadeTest {
    
    //---- Unit under test ----//
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB();
    private StorageFacade storageFacade = new StorageFacadeDB();
    
    String accountType = "CHECK";
    String personKey = "5";
    String bankKey = "1";
    int holdings = 999;

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
        assert(res.size() == 3);
    }

    @Test
    public void testSettersAndGetters() {
        AccountDB acc = new AccountDB();
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
    }
}