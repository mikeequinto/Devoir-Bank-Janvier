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


}