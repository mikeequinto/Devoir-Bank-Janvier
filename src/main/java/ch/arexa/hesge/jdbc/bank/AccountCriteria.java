package ch.arexa.hesge.jdbc.bank;

/**
 * Created by john on 08.12.2018.
 */
public class AccountCriteria {

    private String accountRef;
    private Double balance;
    private String currency;
    private Long idClient;

    public AccountCriteria(String accountRef, Double balance, String currency, Long idClient) {
        this.accountRef = accountRef;
        this.balance = balance;
        this.currency = currency;
        this.idClient = idClient;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public Double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getIdClient() {
        return idClient;
    }
}
