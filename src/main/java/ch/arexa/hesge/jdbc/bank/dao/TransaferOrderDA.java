package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Account;
import ch.arexa.hesge.jdbc.bank.Event;
import ch.arexa.hesge.jdbc.bank.TransferOrder;
import ch.arexa.hesge.jdbc.bank.exceptions.AmountNotAvailableException;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;

import java.sql.*;

/**
 * Created by john on 08.12.2018.
 */
public class TransaferOrderDA extends BankDA {

    AccountDA accountDao = new AccountDA();

    public Long persist(TransferOrder e) throws SQLException {

        try{
            updateAccounts(e.getAccountRef1(), e.getAccountRef2(), new java.sql.Date(e.getOperationDate().getTime()), e.getAmount());
        }catch(AmountNotAvailableException a){
            a.printStackTrace();
        }

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("insert into TransferOrder(accountRefSource, accountRefTarget, amount, executionDate, operationDate) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, e.getAccountRef1());
            pStmt.setString(2, e.getAccountRef2());
            pStmt.setDouble(3, e.getAmount());
            pStmt.setDate(4, new java.sql.Date(e.getExecutionDate().getTime()));
            pStmt.setDate(5, new java.sql.Date(e.getOperationDate().getTime()));
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

    private void updateAccounts(String accountRef1, String accountRef2, Date operationDate, double amount) throws SQLException, AmountNotAvailableException{

        Account a;
        Account a2;
        try{

            a = accountDao.findAccountByRef(accountRef1);
            a2 = accountDao.findAccountByRef(accountRef2);

            if(a.getBalance() - amount < 0){
                throw new AmountNotAvailableException("Amount not available on source account!");
            }
            a.setBalance(a.getBalance() - amount);
            a2.setBalance(a2.getBalance() + amount);
            a.update();
            a2.update();

            Event e1 = new Event(a.getAccountRef(), operationDate, amount, "Debit");
            Event e2 = new Event(a2.getAccountRef(), operationDate, amount, "Credit");
            e1.persist();
            e2.persist();

        }catch(ObjectNotFoundException o){
            System.out.println(o);
        }

    }
}
