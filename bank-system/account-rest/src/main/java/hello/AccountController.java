package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImpl;
import se.liu.ida.tdp024.account.util.kafka.KafkaTopic;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtil;
import se.liu.ida.tdp024.account.util.kafka.KafkaUtilImpl;

import java.util.List;

@RestController
@RequestMapping("account-rest/account")
public class AccountController {
    private final TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    private final AccountEntityFacade accountEntityFacade = new AccountEntityFacadeDB(transactionEntityFacade);
    private final AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacade);
    private final TransactionLogicFacade transactionLogicFacade = new TransactionLogicFacadeImpl(transactionEntityFacade);
    private final KafkaUtil kafkaUtil = new KafkaUtilImpl();

    private final static String FIND_REQUEST = "Received /find/person request";
    private final static String FIND_SUCCESS = "Account search successful. Returning found accounts";
    private final static String CREATE_REQUEST = "Received /create request";
    private final static String CREATE_SUCCESS = "Account creation successful";
    private final static String DEBIT_REQUEST = "Received /debit request";
    private final static String DEBIT_SUCCESS = "Debit transaction successful";
    private final static String CREDIT_REQUEST = "Recieved /credit request";
    private final static String CREDIT_SUCCESS = "Credit transaction successful";
    private final static String TRANSACTION_REQUEST = "Received /transaction request";
    private final static String TRANSACTION_SUCCESS = "Transaction search successful. Returning found transactions";

    @RequestMapping("/find/person")
    public ResponseEntity<List<Account>> findPerson(@RequestParam(value="person") String person) {
        kafkaUtil.publishMessage(KafkaTopic.REST, FIND_REQUEST);
        List<Account> res = accountLogicFacade.findPerson(person);
        kafkaUtil.publishMessage(KafkaTopic.REST, FIND_SUCCESS);
        return new ResponseEntity<List<Account>>(res, HttpStatus.OK);
    }

    @RequestMapping("/create")
    public ResponseEntity<String> create(
            @RequestParam(value="accounttype", defaultValue = "", required = false) String accountType,
            @RequestParam(value="person", defaultValue = "", required = false) String personKey,
            @RequestParam(value="bank", defaultValue = "", required = false) String bankKey) {
        kafkaUtil.publishMessage(KafkaTopic.REST, CREATE_REQUEST);
        String res = accountLogicFacade.create(accountType, personKey, bankKey);
        kafkaUtil.publishMessage(KafkaTopic.REST, CREATE_SUCCESS);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @RequestMapping("/debit")
    public ResponseEntity<String> debit(@RequestParam(value = "id") long id, @RequestParam(value = "amount") int amount) {
        kafkaUtil.publishMessage(KafkaTopic.REST, DEBIT_REQUEST);
        String res = accountLogicFacade.debitAccount(id, amount);
        kafkaUtil.publishMessage(KafkaTopic.REST, DEBIT_SUCCESS);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @RequestMapping("/credit")
    public ResponseEntity<String> credit(@RequestParam(value = "id") long id, @RequestParam(value = "amount") int amount) {
        kafkaUtil.publishMessage(KafkaTopic.REST, CREDIT_REQUEST);
        String res = accountLogicFacade.creditAccount(id, amount);
        kafkaUtil.publishMessage(KafkaTopic.REST, CREDIT_SUCCESS);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

    @RequestMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam(value = "id") long id) {
        kafkaUtil.publishMessage(KafkaTopic.REST, TRANSACTION_REQUEST);
        List<Transaction> res = transactionLogicFacade.getTransactions(id);
        kafkaUtil.publishMessage(KafkaTopic.REST, TRANSACTION_SUCCESS);
        return new ResponseEntity<List<Transaction>>(res, HttpStatus.OK);
    }
}
