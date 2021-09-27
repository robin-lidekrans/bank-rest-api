package se.liu.ida.tdp024.account.data.impl.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import se.liu.ida.tdp024.account.data.api.entity.Account;

@Entity
public class AccountDB implements Account {
    @Id
    @GeneratedValue
    private long id;
    private String personKey;
    private String accountType;
    private String bankKey;
    private int holdings;

    @Override
    public void setPersonKey(String personKey) {
        this.personKey = personKey;
    }

    @Override
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }
    
    @Override
    public void setHoldings(int holding) {
        this.holdings = holding;
    }

    @Override
    public String getPersonKey() {
        return this.personKey;
    }

    @Override
    public String getAccountType() {
        return this.accountType;
    }

    @Override
    public String getBankKey() {
        return this.bankKey;
    }

    @Override
    public int getHoldings() {
        return this.holdings;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
