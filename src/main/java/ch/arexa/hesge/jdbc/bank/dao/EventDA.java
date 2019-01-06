package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Client;
import ch.arexa.hesge.jdbc.bank.Event;
import java.text.DateFormat;

import java.sql.*;
import java.text.SimpleDateFormat;

public class EventDA extends BankDA{

    public Long persist(Event e) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("insert into event(accountRef, operationDate, amount, description) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, e.getAccountRef());
            pStmt.setDate(2, new java.sql.Date(e.getOperationDate().getTime()));
            pStmt.setDouble(3, e.getAmount());
            pStmt.setString(4, e.getDescription());
            pStmt.executeUpdate();
            rs = pStmt.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                return id;
            } else {
                throw new SQLException("Failed to persist entity ");
            }
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

}
