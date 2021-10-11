package hello.test;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hello.AccountController;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;

public class AccountControllerTest {

    //--- Unit under test ---//
    public AccountController accountController = new AccountController();
    public StorageFacade storageFacade = new StorageFacadeDB();
    
    @After
    public void tearDown() {
        if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    private void assertFailed(ResponseEntity<String> res) {
        assert(res.getBody().equals("FAILED"));
    }

    private void assertOk(ResponseEntity<String> res) {
        assert(res.getBody().equals("OK"));
    }

    @Test
    public void testCreate() {
        ResponseEntity<String> res = accountController.create("", "", "");
        assertFailed(res);
        res = accountController.create("CHECK", "555", "NORDEA");
        assertFailed(res);
        res = accountController.create("CREDIT", "2", "NORDEA");
        assertFailed(res);
        res = accountController.create("CHECK", "2", "NORDEA");
        assertOk(res);
    }

    @Test
    public void testFind() {
        ResponseEntity<List<Account>> res = accountController.findPerson("2");
        accountController.create("CHECK", "Xena", "NORDEA");
        res = accountController.findPerson("2");
        Account acc = res.getBody().get(0);
        assert(acc.getAccountType().equals("CHECK"));
        assert(acc.getPersonKey().equals("2"));
        assert(acc.getBankKey().equals("4"));
    }

    @Test
    public void testDebit() {
        accountController.create("CHECK", "2", "NORDEA");
        ResponseEntity<String> res = accountController.debit(1, 20);
        assertFailed(res);
        res = accountController.credit(1, 500);
        assertOk(res);
        res = accountController.debit(1, 200);
        assertOk(res);
        res = accountController.debit(2, 200);
        assertFailed(res);
    }

    @Test
    public void testCredit() {
        accountController.create("CHECK", "2", "NORDEA");
        ResponseEntity<String> res = accountController.credit(1, 20);
        assertOk(res);
        res = accountController.credit(2, 20);
        assertFailed(res);
    }

    @Test
    public void testGetTransactions() {
        accountController.create("CHECK", "2", "NORDEA");
        ResponseEntity<String> res = accountController.credit(1, 500);
        assertOk(res);
        res = accountController.debit(1, 700);
        assertFailed(res);
        res = accountController.debit(1, 250);
        assertOk(res);

        ResponseEntity<List<Transaction>> resTransactionList = accountController.getTransactions(1);
        assert(resTransactionList.getStatusCode() == HttpStatus.OK);
        List<Transaction> transactionList = resTransactionList.getBody();
        assert(transactionList.get(0).getType().equals("CREDIT"));
        assert(transactionList.get(0).getStatus().equals("OK"));

        assert(transactionList.get(1).getType().equals("DEBIT"));
        assert(transactionList.get(1).getStatus().equals("FAILED"));

        assert(transactionList.get(2).getType().equals("DEBIT"));
        assert(transactionList.get(2).getStatus().equals("OK"));
    }
}
