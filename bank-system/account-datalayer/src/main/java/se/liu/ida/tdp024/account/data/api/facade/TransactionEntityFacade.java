package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

import java.util.List;

public interface TransactionEntityFacade {

    public String create(String type, int amount, String created, String status, Account account);

    public List<Transaction> get(Long accountId);

}
