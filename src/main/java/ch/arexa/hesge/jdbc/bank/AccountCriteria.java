package ch.arexa.hesge.jdbc.bank;

/**
 * Created by john on 08.12.2018.
 */
public class AccountCriteria {

    private String accountRef;
    private Double balance;
    private String currency;
    private Long idClient;

    public AccountCriteria() {}

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

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
}
