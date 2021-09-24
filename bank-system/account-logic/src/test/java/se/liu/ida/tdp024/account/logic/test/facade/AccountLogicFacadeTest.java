package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.After;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

public class AccountLogicFacadeTest {


    //--- Unit under test ---//
    public AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    public StorageFacade storageFacade;

    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }



    @Test
    public void testCreate() {
      String res;

      // Valid account, person, bank
      res = accountLogicFacade.create("CHECK", "1", "NORDEA");
      assert(res == "OK");

      // Valid account, bank, invalid person
      res = accountLogicFacade.create("CHECK", "1337", "NORDEA");
      assert(res == "FAILED");

      // Valid account, person, invalid bank
      res = accountLogicFacade.create("CHECK", "1", "DANSKEBANK");
      assert(res == "FAILED");
    }
}
