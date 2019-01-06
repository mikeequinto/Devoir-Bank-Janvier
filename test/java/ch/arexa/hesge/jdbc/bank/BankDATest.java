package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.AccountDA;
import ch.arexa.hesge.jdbc.bank.dao.BankDA;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.Date;
import java.util.*;


public class BankDATest extends DBTest {
    Logger logger = Logger.getLogger(BankDATest.class);

    BankDA dao = new BankDA();
    AccountDA daoAccount = new AccountDA();

    @Before
    public void init() throws Exception {
        Connection con = dao.getConnection();
        reinitMySQL(con);
        dao.closeAll(con, null, null);
    }


    @Test
    public void getConnection() throws SQLException {
        Connection con = dao.getConnection();
        logger.info(con.getMetaData().getDatabaseProductName());
        dao.closeAll(con, null, null);
    }


    @Test
    public void stmt() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            rs = displayData(stmt);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            dao.closeAll(con, stmt, rs);

        }

    }

    @Test
    public void insert() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate("insert into client values(3, 'fred', 'rue du lac')");
            stmt = con.createStatement();
            rs = displayData(stmt);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            dao.closeAll(con, stmt, rs);
        }
    }


    @Test
    public void insertPStmt() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Statement stmt2 = null;
        ResultSet rs2 = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("insert into client values(?, ?, ?)");
            stmt.setInt(1, 12);
            stmt.setString(2, "Patricia12");
            stmt.setString(3, "place de l'église12");
            stmt.executeUpdate();

            //con.commit();
            stmt2 = con.createStatement();
            rs2 = displayData(stmt);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            dao.closeAll(con, stmt, rs);
            dao.closeAll(con, stmt2, rs2);
        }
    }


    @Test
    public void preparedStmt() {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = dao.getConnection();
            stmt = con.prepareStatement("select * from client where name = ?");
            stmt.setString(1, "Lucie");

            rs = stmt.executeQuery();
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            dao.closeAll(con, stmt, rs);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            dao.closeAll(con, stmt, rs);

        }
    }

    private ResultSet displayData(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery("select * from client");
        while (rs.next()) {
            System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
        }
        return rs;
    }

    @Test
    public void insertClient(){

        Client c = new Client("john", "3 place de la mairie");
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
    public void transferOrder(){

        //Client 1
        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        Account a = new Account(UUID.randomUUID().toString(), 500.0, "CHF");
        a.setIdClient(id);
        a.persist();

        //Client 2
        Client c2 = new Client("test", "4 rue du rhone");
        c.persist();
        long id2 = c.getId();
        Account a2 = new Account(UUID.randomUUID().toString(), 0.0, "CHF");
        a.setIdClient(id2);
        a.persist();

        //Transfer
        //TransferOrder t = new TransferOrder(a.getAccountRef(), a2.getAccountRef(), 200.0, );

    }

    @Test
    public void getAccountsByAccountCriteria() throws SQLException{

        //Client 1
        Client c = new Client("test", "4 rue du rhone");
        c.persist();
        long id = c.getId();
        //Account 1
        Account a = new Account(UUID.randomUUID().toString(), 500.0, "CHF");
        a.setIdClient(id);
        a.persist();
        //Account 2
        Account a2 = new Account(UUID.randomUUID().toString(), 500.0, "CHF");
        a2.setIdClient(id);
        a2.persist();
        //Account 3
        Account a3 = new Account(UUID.randomUUID().toString(), 450.0, "CHF");
        a3.setIdClient(id);
        a3.persist();

        AccountCriteria ac = new AccountCriteria(a.getAccountRef(), a.getBalance(), a.getCurrency(), a.getIdClient());
        List<Account> aLst = daoAccount.find(ac);

        Assert.assertTrue(aLst.contains(a));
        Assert.assertTrue(aLst.contains(a2));
        Assert.assertEquals(2, aLst.size());

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