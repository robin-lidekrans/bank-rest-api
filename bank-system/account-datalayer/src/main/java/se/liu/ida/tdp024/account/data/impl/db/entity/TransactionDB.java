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

    private String type;
    private int amount;
    private String created;
    private String status;

    @ManyToOne(targetEntity = AccountDB.class)
    private Account account;

    // =========== Setters ===========
    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void setCreated(String timeStamp) {
        this.created = timeStamp;
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
    public String getType() {
        return this.type;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public String getCreated() {
        return this.created;
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
