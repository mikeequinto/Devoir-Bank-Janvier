package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Account;
import ch.arexa.hesge.jdbc.bank.AccountCriteria;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;
import ch.arexa.hesge.jdbc.bank.helper.*;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class AccountDA extends BankDA {

    private static final String ACCOUNT_REF = "accountRef";
    private static final String ACCOUNT_BALANCE = "balance";
    private static final String ACCOUNT_CURR = "currencyCode";
    private static final String ACCOUNT_CLIENT_ID = "idClient";

    Logger log = Logger.getLogger(AccountDA.class);

    public void persist(Account acc) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("insert into Account(accountRef, idClient,balance, currencyCode) values (?,?, ?,?)");
            pStmt.setString(1, acc.getAccountRef());
            pStmt.setLong(2, acc.getIdClient());
            pStmt.setDouble(3, acc.getBalance());
            pStmt.setString(4, acc.getCurrency());

            pStmt.executeUpdate();
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

    public void update(Account acc) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("update Account set idClient=?, balance=?, currencyCode=? where accountRef=?");
            pStmt.setLong(1, acc.getIdClient());
            pStmt.setDouble(2, acc.getBalance());
            pStmt.setString(3, acc.getCurrency());
            pStmt.setString(4, acc.getAccountRef());

            pStmt.executeUpdate();
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

    public Account findAccountByRef(String accountRef) throws SQLException, ObjectNotFoundException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pStmt = con.prepareStatement("select * from Account where accountRef=?");
            pStmt.setString(1, accountRef);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                Account account = new Account(rs.getString(ACCOUNT_REF), rs.getDouble(ACCOUNT_BALANCE), rs.getString(ACCOUNT_CURR));
                account.setIdClient(rs.getLong(ACCOUNT_CLIENT_ID));
                return account;
            } else {
                throw new ObjectNotFoundException("Failed to  find account with ref " + accountRef);
            }
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

    public List<Account> findAccountsByClientId(Long idClient) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            List<Account> accounts = new ArrayList();
            con = getConnection();
            pStmt = con.prepareStatement("select * from Account where idClient=?");
            pStmt.setLong(1, idClient);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getString(ACCOUNT_REF), rs.getDouble(ACCOUNT_BALANCE), rs.getString(ACCOUNT_CURR));
                account.setIdClient(rs.getLong(ACCOUNT_CLIENT_ID));
                accounts.add(account);
            }
            return accounts;
        } finally {
            closeAll(con, pStmt, rs);
        }
    }

    public List<Account> findAccounts(AccountCriteria accountCriteria) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            List<Account> accounts = new ArrayList();
            con = getConnection();
            pStmt=buildPreparedStatment(accountCriteria, con);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getString(ACCOUNT_REF), rs.getDouble(ACCOUNT_BALANCE), rs.getString(ACCOUNT_CURR));
                account.setIdClient(rs.getLong(ACCOUNT_CLIENT_ID));
                accounts.add(account);
            }
            return accounts;
        } finally {
            closeAll(con, pStmt, rs);
        }
    }


    // Solution a améliorer, très limité en possibilités dans l'état

    public PreparedStatement buildPreparedStatment(AccountCriteria accountCriteria, Connection con) throws SQLException{

        List<Pair<Type, Object>>  params = new ArrayList();

        StringBuilder req = new StringBuilder();
        req.append("select * from Account where 1=1 ");

        if(accountCriteria.getAccountRef()!=null){
            req.append(" and accountRef=?");
            params.add(new Pair(Type.STRING,accountCriteria.getAccountRef() ));
        }
        if(accountCriteria.getBalance()!=null) {
            req.append(" and balance=?");
            params.add(new Pair(Type.DOUBLE,accountCriteria.getBalance() ));
        }
        if(accountCriteria.getCurrency()!=null) {
            req.append(" and currencyCode=?");
            params.add(new Pair(Type.STRING,accountCriteria.getCurrency() ));
        }
        if(accountCriteria.getIdClient()!=null) {
            req.append(" and idClient=?");
            params.add(new Pair(Type.LONG,accountCriteria.getIdClient() ));
        }

        String sqlRequest = req.toString();
        log.info("built SQL : "+sqlRequest);
        PreparedStatement pStmt = con.prepareStatement(sqlRequest);
        return assignValues(pStmt, params);

    }

    private PreparedStatement assignValues( PreparedStatement pStmt, List<Pair<Type, Object>> params) throws SQLException {
        for (int i=0; i < params.size(); i++ ){
            switch(params.get(i).getFirst()){
                case INT:  pStmt.setInt(i+1,(Integer) params.get(i).getSecond()); break;
                case DOUBLE:  pStmt.setDouble(i+1,(Double) params.get(i).getSecond()); break;
                case DATE:  pStmt.setDate(i+1, DateHelper.toSqlDate((Date)params.get(i).getSecond())); break;
                case STRING:  pStmt.setString(i+1,(String) params.get(i).getSecond()); break;
                default: throw new RuntimeException("Unknown data type ");
            }
        }
        log.info("Prepared statment "+pStmt);
        return pStmt;
    }

}
