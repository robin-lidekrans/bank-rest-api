package se.liu.ida.tdp024.account.logic.api.facade;

import java.lang.String;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountLogicFacade {
    public List<Account> findPerson(String person);

    public String create(String accountType, String personKey, String bankKey);
}
