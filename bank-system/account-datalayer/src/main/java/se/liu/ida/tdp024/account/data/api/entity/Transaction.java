package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Transaction extends Serializable {

    // Setters
    public void setType(String type);

    public void setAmount(int amount);

    public void setCreated(String timeStamp);

    public void setStatus(String status);

    public void setAccount(Account account);

    // Getters
    public String getType();

    public int getAmount();

    public String getCreated();

    public String getStatus();

    public Account getAccount();

    public long getId();
    
}
