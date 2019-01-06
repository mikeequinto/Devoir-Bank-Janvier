package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.TransaferOrderDA;
import ch.arexa.hesge.jdbc.bank.exceptions.AmountNotAvailableException;
import ch.arexa.hesge.jdbc.bank.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class TransferOrder {
    private  Long idTransferOrder = null;
    private  String accountRef1;
    private  String accountRef2;
    private  Double amount;
    private  Date executionDate;
    private  Date operationDate;

    TransaferOrderDA dao = new TransaferOrderDA();

    public TransferOrder(String accountRef1, String accountRef2, Double amount, Date executionDate, Date operationDate) {
        this.accountRef1 = accountRef1;
        this.accountRef2 = accountRef2;
        this.amount = amount;
        this.executionDate = executionDate;
        this.operationDate = operationDate;
    }

    public long getIdTransferOrder() {
        return idTransferOrder;
    }

    public void setIdTransferOrder(long idTransferOrder) {
        this.idTransferOrder = idTransferOrder;
    }

    public String getAccountRef1() {
        return accountRef1;
    }

    public void setAccountRef1(String accountRef1) {
        this.accountRef1 = accountRef1;
    }

    public String getAccountRef2() {
        return accountRef2;
    }

    public void setAccountRef2(String accountRef2) {
        this.accountRef2 = accountRef2;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public void persist() throws PersistenceException {
        try {
            if(idTransferOrder != null){
                throw new PersistenceException("Object is already persisted, use update instead");
            }
            Long id = dao.persist(this);
            this.setIdTransferOrder(id);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to save "+this, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferOrder that = (TransferOrder) o;
        return idTransferOrder == that.idTransferOrder &&
                Objects.equals(accountRef1, that.accountRef1) &&
                Objects.equals(accountRef2, that.accountRef2) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(executionDate, that.executionDate) &&
                Objects.equals(operationDate, that.operationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransferOrder, accountRef1, accountRef2, amount, executionDate, operationDate);
    }


    @Override
    public String toString() {
        return "ch.arexa.hesge.jdbc.bank.TransferOrder{" +
                "idTransferOrder=" + idTransferOrder +
                ", accountRef1='" + accountRef1 + '\'' +
                ", accountRef2='" + accountRef2 + '\'' +
                ", amount=" + amount +
                ", executionDate=" + executionDate +
                ", operationDate=" + operationDate +
                '}';
    }
}
