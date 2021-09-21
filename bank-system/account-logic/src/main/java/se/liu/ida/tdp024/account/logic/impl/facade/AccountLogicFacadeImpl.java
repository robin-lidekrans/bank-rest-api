package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;

public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private AccountEntityFacade accountEntityFacade;
    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public List<Account> findPerson(String person) {
        return accountEntityFacade.get(person);
    }

    @Override
    public String create(String accountType, String personKey, String bankKey) {
        return accountEntityFacade.create(accountType, personKey, bankKey);
    }

}
