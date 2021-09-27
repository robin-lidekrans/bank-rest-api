package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;

public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    private AccountEntityFacade accountEntityFacade;
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

        return accountEntityFacade.create(accountType, personKey, bankKey);
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
