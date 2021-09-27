package se.liu.ida.tdp024.account.data.test.facade;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;

public class TransactionEntityFacadeTest {
        
    //---- Unit under test ----//
    private TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    private AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
    private StorageFacade storageFacade = new StorageFacadeDB();

    // Account 1
    private Account acc1;
    private String accountType = "CHECK";
    private String personKey = "1";
    private String bankKey = "2";

    // Account 2
    private Account acc2;
    private String accountType2 = "CREDIT";
    private String personKey2 = "2";
    private String bankKey2 = "4";

    @Before
    public void setup() {
        accountEntityFacade.create(accountType, personKey, bankKey);
        acc1 = accountEntityFacade.get(personKey).get(0);

        accountEntityFacade.create(accountType2, personKey2, bankKey2);
        acc2 = accountEntityFacade.get(personKey2).get(0);
    }

    @After
    public void tearDown() {
       storageFacade.emptyStorage();
    }

    @Test
    public void testCreate() {
        String res = transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:32", "OK", acc1);
        assert(res.equals("OK"));
        res = transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:33", "OK", acc1);
        assert(res.equals("OK"));
        res = transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:32", "OK", acc2);
        assert(res.equals("OK"));
    }

    @Test
    public void testGet() {
        transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:32", "OK", acc1);
        transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:32", "OK", acc1);
        transactionEntityFacade.create("CREDIT", 5, "2013-06-30 14:49:32", "OK", acc2);

        List<Transaction> res = transactionEntityFacade.get(acc1.getId());
        assert(res.size() == 2);
        assert(res.get(0).getAccount() == res.get(1).getAccount());
        assert(res.get(0).getAmount() == res.get(1).getAmount());
        assert(res.get(0).getStatus().equals(res.get(1).getStatus()));
        assert(res.get(0).getTimeStamp().equals(res.get(1).getTimeStamp()));
        assert(res.get(0).getTransactionType().equals(res.get(1).getTransactionType()));

        res = transactionEntityFacade.get(acc2.getId());
        assert(res.size() == 1);

        res = transactionEntityFacade.get((long) 14553);
        assert(res.size() == 0);
    }
}
