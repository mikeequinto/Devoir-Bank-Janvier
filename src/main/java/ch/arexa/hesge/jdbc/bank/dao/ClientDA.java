package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Client;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;

import java.sql.*;

public class ClientDA extends BankDA {


    public static final String CLIENT_ID = "IdClient";
    public static final String CLIENT_NAME = "name";
    public static final String CLIENT_ADDRESS = "address";

    public Long persist(Client c) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("insert into Client(name,address) values (?,?)", Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, c.getName());
            pStmt.setString(2, c.getAddress());
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

    public void update(Client c) throws SQLException {

        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("update Client set name=?, address=? where idClient=?");
            pStmt.setString(1, c.getName());
            pStmt.setString(2, c.getAddress());
            pStmt.setLong(3, c.getId());
            pStmt.executeUpdate();
        } finally {
            closeAll(con, pStmt, null);
        }
    }


    public Client findClientById(Long id) throws SQLException, ObjectNotFoundException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("select * from Client where idClient=?");
            pStmt.setLong(1, id);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(rs.getString(CLIENT_NAME), rs.getString(CLIENT_ADDRESS));
                client.setId(rs.getLong(CLIENT_ID));
                return client;
            } else {
                throw new ObjectNotFoundException("Failed to find client with id " + id);
            }
        } finally {
            closeAll(con, pStmt, rs);
        }
    }
}
