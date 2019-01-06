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
import java.sql.SQLException;
import java.util.Date;
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
    public void insertClient(){

        Client c = new Client("john5", "3 place de la mairie");
        Client c1 = new Client("test2", "4 rue du rhone");
        c.persist();
        c1.persist();
    }

    @Test
    public void createAccount() {

        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        Account a = new Account(UUID.randomUUID().toString(), 0.0, "CHF");
        a.setIdClient(id);
        a.persist();

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


    @Test
    public void findByCriteria() throws Exception, ObjectNotFoundException {
        Client pascal = new Client("pascal", "pascal@gmail.com");
        pascal.persist();
        Account acc1 = new Account(UUID.randomUUID().toString(), Double.valueOf(100), "CHF");
        acc1.setIdClient(pascal.getId());
        acc1.persist();

        AccountCriteria criteria= new AccountCriteria();
        criteria.setCurrency("EUR");
        criteria.setBalance(Double.valueOf(200));
        List<Account> accounts = accountDAO.findAccounts(criteria);
        Assert.assertEquals(0, accounts.size());
        
        criteria= new AccountCriteria();
        criteria.setCurrency("CHF");
        criteria.setBalance(Double.valueOf(100));
        accounts = accountDAO.findAccounts(criteria);
        Assert.assertEquals(1, accounts.size());
        acc1=accounts.get(0);
        Assert.assertEquals("CHF", acc1.getCurrency());
        Assert.assertEquals(Double.valueOf(100), acc1.getBalance(),0.01);

    }

    @Test
    public void transferTest() throws SQLException{

        //Client 1
        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        //Account 1
        Account a = new Account(UUID.randomUUID().toString(), 500.0, "CHF");
        a.setIdClient(id);
        a.persist();
        //Client 2
        Client c2 = new Client("test2", "4 rue du rhone");
        c2.persist();
        long id2 = c2.getId();
        //Account 2
        Account a2 = new Account(UUID.randomUUID().toString(), 100.0, "CHF");
        a2.setIdClient(id2);
        a2.persist();

        //TransferOrder
        Date date = new Date();
        TransferOrder to = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 100.0, date, date);
        to.persist();
    }

}