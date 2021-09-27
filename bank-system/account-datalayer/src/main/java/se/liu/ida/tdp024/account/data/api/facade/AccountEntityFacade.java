package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;

import java.util.List;

public interface AccountEntityFacade {

    public String create(String accountType, String personKey, String bankKey);

    public List<Account> get(String personKey);

    public Account getAccount(long accountID);

    public String debitAccount(Long accountID, int amount);
    
    public String creditAccount(Long accountID, int amount);
    
}
