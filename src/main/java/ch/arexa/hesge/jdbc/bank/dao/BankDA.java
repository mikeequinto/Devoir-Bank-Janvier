package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.exceptions.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;


public class BankDA {


    Logger logger = Logger.getLogger(BankDA.class);


    public Connection getConnection(String url, String user, String password) {
        Connection con = null;
        try {
            // MySql : com.mysql.jdbc.Driver
            // Oracle : oracle.jdbc.OracleDriver
            // PostgreSQL : org.postgersql.Driver
            // hsql : org.hsqldb.jdbcDriver
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public Connection getConnection() {
        // MySql :"jdbc:mysql://localhost:3306/NomBase"
        // Oracle :"jdbc:oracle:thin:@localhost:1521:NomInstance"
        // hsql : jdbc:hsqldb:file:testdb
        // jdbc:hsqldb:mem:mymemdb
        return getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
    }





    public void closeAll(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new PersistenceException("Failed to close JDBC resources", e);
        }
    }
}