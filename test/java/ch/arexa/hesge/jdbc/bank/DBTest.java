package ch.arexa.hesge.jdbc.bank;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {


    public void reinitMySQL(Connection con) throws SQLException {
        con.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
        con.createStatement().executeUpdate("truncate table account");
        con.createStatement().executeUpdate("truncate table client");
        con.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
    }
}
