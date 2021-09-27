package se.liu.ida.tdp024.account.logic.api.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;

public interface TransactionLogicFacade {
    
    public List<Transaction>getTransactions(long accountId);
    
}
