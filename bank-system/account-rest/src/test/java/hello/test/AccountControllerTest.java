package hello.test;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hello.AccountController;
import se.liu.ida.tdp024.account.data.impl.db.util.StorageFacadeDB;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;

public class AccountControllerTest {

    //--- Unit under test ---//
    public AccountController accountController = new AccountController();
    public StorageFacade storageFacade = new StorageFacadeDB();
    public AccountJsonSerializer jsonSerializer = new AccountJsonSerializerImpl();
    
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

    private void assertEmptyList(ResponseEntity<String> res) {
        assert(res.getBody().equals("[]"));
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
        ResponseEntity<String> res = accountController.findPerson("2");
        assertEmptyList(res);
        accountController.create("CHECK", "Xena", "NORDEA");
        res = accountController.findPerson("2");
        List<LinkedHashMap<String, String>> accountList = jsonSerializer.fromJson(res.getBody(), List.class);
        assert(accountList.size() == 1);
        LinkedHashMap<String, String> acc = accountList.get(0);
        assert(acc.get("accountType").equals("CHECK"));
        assert(acc.get("personKey").equals("2"));
        assert(acc.get("bankKey").equals("4"));
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

        res = accountController.getTransactions(1);
        List<LinkedHashMap<String, String>> transactionList = jsonSerializer.fromJson(res.getBody(), List.class);
        assert(transactionList.get(0).get("type").equals("CREDIT"));
        assert(transactionList.get(0).get("status").equals("OK"));

        assert(transactionList.get(1).get("type").equals("DEBIT"));
        assert(transactionList.get(1).get("status").equals("FAILED"));

        assert(transactionList.get(2).get("type").equals("DEBIT"));
        assert(transactionList.get(2).get("status").equals("OK"));
    }
}
