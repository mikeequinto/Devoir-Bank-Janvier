package ch.arexa.hesge.jdbc.bank;

import ch.arexa.hesge.jdbc.bank.dao.ClientDA;
import ch.arexa.hesge.jdbc.bank.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.Objects;

public class Client{

    private Long id;
    private String name;
    private String address;
private

ClientDA dao= new ClientDA();


    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void persist() throws PersistenceException {
        try {
            if(this.getId()!=null){
                throw new PersistenceException("Object is already persisted, use update instead");
            }
            Long id = dao.persist(this);
            this.setId(id);
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
        return "ch.arexa.hesge.jdbc.bank.Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(name, client.name) &&
                Objects.equals(address, client.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address);
    }
}
