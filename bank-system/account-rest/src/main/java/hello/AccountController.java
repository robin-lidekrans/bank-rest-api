
package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("account-rest/account")
public class AccountController {

    private final AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    private final AccountJsonSerializer accountJsonSerializer= new AccountJsonSerializerImpl();

    @RequestMapping("/find/person")
    public ResponseEntity<String> findPerson(@RequestParam(value="person") String person) {
        List<Account> res = new ArrayList<Account>();
        res = accountLogicFacade.findPerson(person);
        return new ResponseEntity<String>(accountJsonSerializer.toJson(res), HttpStatus.OK);
    }

    @RequestMapping("/create")
    public ResponseEntity<String> create(
            @RequestParam(value="accounttype", defaultValue = "", required = false) String accountType,
            @RequestParam(value="person", defaultValue = "", required = false) String personKey,
            @RequestParam(value="bank", defaultValue = "", required = false) String bankKey) {
        String res = accountLogicFacade.create(accountType, personKey, bankKey);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @RequestMapping("debit")
    public ResponseEntity<String> debit(@RequestParam(value = "id") long id, @RequestParam(value = "amount") int amount) {
        return null;
    }

    @RequestMapping("credit")
    public ResponseEntity<String> credit(@RequestParam(value = "id") long id, @RequestParam(value = "amount") int amount) {
        return null;
    }

    @RequestMapping("transactions")
    public ResponseEntity<String> getTransactions(@RequestParam(value = "id") long id) {
        return null;
    }
}
