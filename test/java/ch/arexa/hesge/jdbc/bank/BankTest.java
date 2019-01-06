package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.AccountDA;
import ch.arexa.hesge.jdbc.bank.dao.BankDA;
import ch.arexa.hesge.jdbc.bank.dao.ClientDA;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class BankTest extends DBTest {

    private static final Logger logger = Logger.getLogger(BankDA.class);
    AccountDA accountDAO = new AccountDA();
    ClientDA clientDAO = new ClientDA();

    @Before
    public void init() throws Exception {
        Connection con = accountDAO.getConnection();
        reinitMySQL(con);
        accountDAO.closeAll(con,null,null);
    }


    @Test
    public void persistCient() throws Exception, ObjectNotFoundException {
        Client pierre = new Client("pierre", "pierre@gmail.com");
        pierre.persist();
        Client c = clientDAO.findClientById(pierre.getId());
        Assert.assertEquals(c, pierre);
        pierre.setAddress("rue du lac");
        pierre.update();
    }

    @Test
    public void addAccountToClient() throws Exception, ObjectNotFoundException {
        Client pascal = new Client("pascal5", "pascal@gmail.com");
        pascal.persist();
        Account acc1 = new Account(UUID.randomUUID().toString(), Double.valueOf(100), "CHF");
        acc1.setIdClient(pascal.getId());
        acc1.persist();


        Account account = accountDAO.findAccountByRef(acc1.getAccountRef());
        Assert.assertEquals(acc1, account);

        acc1.setCurrency("EUR");
        acc1.update();
        account = accountDAO.findAccountByRef(acc1.getAccountRef());
        Assert.assertEquals("EUR", account.getCurrency());


        List<Account> accounts = accountDAO.findAccountsByClientId(pascal.getId());
        Assert.assertEquals(1, accounts.size());
        Assert.assertTrue(accounts.contains(acc1));


        Account acc2 = new Account(UUID.randomUUID().toString(), Double.valueOf(200), "EUR");
        acc2.setIdClient(pascal.getId());
        acc2.persist();

        accounts = accountDAO.findAccountsByClientId(pascal.getId());
        Assert.assertEquals(2, accounts.size());
        Assert.assertTrue(accounts.contains(acc1));
        Assert.assertTrue(accounts.contains(acc2));

    }

}