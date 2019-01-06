package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.AccountDA;
import ch.arexa.hesge.jdbc.bank.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.Objects;

public class Account {
   private String accountRef;
   private Double balance;
   private String currency;
   private Long idClient;

    AccountDA dao= new AccountDA();

    public Account(String accountRef, Double balance, String currency) {
        this.accountRef = accountRef;
        this.balance = balance;
        this.currency = currency;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public Double getBalance() {
        return balance;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void persist() throws PersistenceException {
        try {
            dao.persist(this);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to save "+this, e);
        }
    }

    public void update() throws PersistenceException {
        try {
            dao.update(this);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to update "+this, e);
        }
    }


    @Override
    public String toString() {
        return "ch.arexa.hesge.jdbc.bank.Account{" +
                "accountRef='" + accountRef + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountRef, account.accountRef) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(currency, account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountRef, balance, currency);
    }
}