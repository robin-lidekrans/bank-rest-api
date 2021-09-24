
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

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    private final AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());

    @RequestMapping("/account-rest/account/find/person")
    public ResponseEntity<List<Account>> findPerson(@RequestParam(value="person") String person) {
        List<Account> res = new ArrayList<Account>();
        res = accountLogicFacade.findPerson(person);
        return new ResponseEntity<List<Account>>(res, HttpStatus.OK);
    }

    @RequestMapping("/account-rest/account/create")
    public ResponseEntity<String> create(@RequestParam(value="accounttype") String accountType,
                                         @RequestParam(value="person") String personKey,
                                         @RequestParam(value="bank") String bankKey) {
        String res = accountLogicFacade.create(accountType, personKey, bankKey);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }
}