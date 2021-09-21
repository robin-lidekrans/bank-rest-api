package se.liu.ida.tdp024.account.logic.api.facade;

import java.lang.String;
import java.util.List;

public interface AccountLogicFacade {
    public List findPerson(String person);

    public String create(String accountType, String personKey, String bankKey);
}
