package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;

public class TransactionLogicFacadeImpl implements TransactionLogicFacade {

    private TransactionEntityFacade transactionEntityFacade;

    public TransactionLogicFacadeImpl(TransactionEntityFacade transactionEntityFacade) {
        this.transactionEntityFacade = transactionEntityFacade;
    }

    @Override
    public List<Transaction> getTransactions(long accountId) {
        return this.transactionEntityFacade.get(accountId);
    }
    
}
