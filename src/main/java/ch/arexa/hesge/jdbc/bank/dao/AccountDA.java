package ch.arexa.hesge.jdbc.bank.dao;

import ch.arexa.hesge.jdbc.bank.Account;
import ch.arexa.hesge.jdbc.bank.AccountCriteria;
import ch.arexa.hesge.jdbc.bank.exceptions.ObjectNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDA extends BankDA {

    private static final String ACCOUNT_REF = "accountRef";
    private static final String ACCOUNT_BALANCE = "balance";
    private static final String ACCOUNT_CURR = "currencyCode";
    private static final String ACCOUNT_CLIENT_ID = "idClient";


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

    public List<Account> find(AccountCriteria ac) throws SQLException{

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        try{
            List<Account> accountList = new ArrayList();
            StringBuilder sb = new StringBuilder();
            int criterias = 0;

            con = getConnection();

            sb.append("select * from account where 1 = 1 ");
            /*if(ac.getAccountRef() != null){
                sb.append("and accountRef = ?");
                criterias++;
            }*/
            if(ac.getBalance() != null){
                sb.append("and balance = ? ");
                criterias++;
            }
            if(ac.getCurrency() != null){
                sb.append("and currencyCode = ? ");
                criterias++;
            }
            if(ac.getIdClient() != null){
                sb.append("and idClient = ? ");
                criterias++;
            }

            //Créer classe pair générique Pair<type, object>

            //sb.append("select * from account where accountRef = ? and balance = ? and currencyCode = ? and idClient = ?");
           //sb.append("select * from account where balance = ? and currencyCode = ? and idClient = ?");
            pStmt = con.prepareStatement(sb.toString());

            //pStmt.setString(1, ac.getAccountRef());
            pStmt.setDouble(1, ac.getBalance());
            pStmt.setString(2, ac.getCurrency());
            pStmt.setLong(3, ac.getIdClient());

            rs = pStmt.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getString(ACCOUNT_REF), rs.getDouble(ACCOUNT_BALANCE), rs.getString(ACCOUNT_CURR));
                account.setIdClient(rs.getLong(ACCOUNT_CLIENT_ID));
                accountList.add(account);
            }
            return accountList;
        }finally {
            closeAll(con, pStmt, rs);
        }

    }

}
