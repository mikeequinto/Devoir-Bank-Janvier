package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.EventDA;
import ch.arexa.hesge.jdbc.bank.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.Date;

public class Event {

    private Long idEvent;
    private String accountRef;
    private Date operationDate;
    private Date valueDate;
    private String operationCode;
    private Double amount;
    private String description;

    EventDA dao = new EventDA();

    public Event(String accountRef, Date operationDate, Double amount, String description){
        this.accountRef = accountRef;
        this.operationDate = operationDate;
        this.amount = amount;
        this.description = description;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public String getAccountRef() {
        return accountRef;
    }

    public void setAccountRef(String accountRef) {
        this.accountRef = accountRef;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void persist() throws PersistenceException {
        try {
            if(this.getIdEvent()!=null){
                throw new PersistenceException("Object is already persisted, use update instead");
            }
            Long id = dao.persist(this);
            this.setIdEvent(id);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to save "+this, e);
        }
    }
}