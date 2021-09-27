package se.liu.ida.tdp024.account.data.impl.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

@Entity
public class TransactionDB implements Transaction {

    // ===========Fields ===========
    @Id
    @GeneratedValue()
    private long id;

    private String transactionType;
    private int transactionAmount;
    private String timeStamp;
    private String status;

    @ManyToOne(targetEntity = AccountDB.class)
    private Account account;

    // =========== Setters ===========
    @Override
    public void setTransactionType(String type) {
        this.transactionType = type;
    }

    @Override
    public void setAmount(int amount) {
        this.transactionAmount = amount;
    }

    @Override
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    // =========== Getters ===========
    @Override
    public String getTransactionType() {
        return this.transactionType;
    }

    @Override
    public int getAmount() {
        return this.transactionAmount;
    }

    @Override
    public String getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public Account getAccount() {
        return this.account;
    }
    
    @Override
    public long getId() {
        return this.id;
    }
}
