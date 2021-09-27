package se.liu.ida.tdp024.account.logic.api.facade;

import java.lang.String;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface AccountLogicFacade {
    public List<Account> findPerson(String personKey);

    public String create(String accountType, String personKey, String bankKey);

    public String debitAccount(Long accountID, int amount);

    public String creditAccount(Long accountID, int amount);
}
