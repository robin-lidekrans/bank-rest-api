package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.HashMap;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;

public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    private AccountEntityFacade accountEntityFacade;
    private AccountJsonSerializer jsonSerializer = new AccountJsonSerializerImpl();
    private String personApiEndpoint = "http://localhost:8060/person/";
    private String bankApiEndpoint = "http://localhost:8070/bank/";
    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public List<Account> findPerson(String personKey) {
        return accountEntityFacade.get(personKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String create(String accountType, String personKey, String bankKey) {
        if (accountType == null || personKey == null || bankKey == null) {
            return "FAILED";
        }

        // Check that the provided person and bank are valid
        String personApiResp = httpHelper.get(personApiEndpoint + "find." + personKey);
        String bankApiResp = httpHelper.get(bankApiEndpoint + "find." + bankKey);
        boolean invalidApiResp = (personApiResp.equals("null") || bankApiResp.equals("null"));
        

        // Check that accountType is valid
        boolean validAccountType = (accountType.equals("CHECK") || accountType.equals("SAVINGS"));

        if (invalidApiResp || !validAccountType) {
            return "FAILED";
        }

        HashMap<String, String> personApiRespMap = jsonSerializer.fromJson(personApiResp, HashMap.class);
        HashMap<String, String> bankApiRespMap = jsonSerializer.fromJson(bankApiResp, HashMap.class);
        String fetchedPersonKey = personApiRespMap.get("Key");
        String fetchedBankKey = bankApiRespMap.get("Key");

        return accountEntityFacade.create(accountType, fetchedPersonKey, fetchedBankKey);
    }

    @Override
    public String debitAccount(Long accountID, int amount) {
        Account account = accountEntityFacade.getAccount(accountID);
        if (account == null) {
            return "FAILED";
        }
        return accountEntityFacade.debitAccount(accountID, amount);
    }

    @Override
    public String creditAccount(Long accountID, int amount) {
        Account account = accountEntityFacade.getAccount(accountID);
        if (account == null) {
            return "FAILED";
        }
        return accountEntityFacade.creditAccount(accountID, amount);
    }
}
