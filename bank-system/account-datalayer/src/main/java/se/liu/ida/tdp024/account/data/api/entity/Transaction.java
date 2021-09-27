package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Transaction extends Serializable {

    // Setters
    public void setTransactionType(String type);

    public void setAmount(int amount);

    public void setTimeStamp(String timeStamp);

    public void setStatus(String status);

    public void setAccount(Account account);

    // Getters
    public String getTransactionType();

    public int getAmount();

    public String getTimeStamp();

    public String getStatus();

    public Account getAccount();

    public long getId();
    
}
