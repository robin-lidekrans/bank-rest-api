package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Account extends Serializable {

    public void setPersonKey(String personKey);
    
    public void setAccountType(String accountType);

    public void setBankKey(String bankKey);

    public void setHoldings(int holding);

    public String getPersonKey();

    public String getAccountType();

    public String getBankKey();

    public int getHoldings();
}
