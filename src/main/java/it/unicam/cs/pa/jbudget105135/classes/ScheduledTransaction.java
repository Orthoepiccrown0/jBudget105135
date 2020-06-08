package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Date;
import java.util.UUID;

public class ScheduledTransaction implements IScheduledTransaction {

    private String ID;
    private String description;
    private ITransaction transaction;
    private boolean completed;


    public ScheduledTransaction(String description, ITransaction transactions) {
        this.ID = UUID.randomUUID().toString();
        this.description = description;
        this.transaction = transactions;
        this.completed = false;
    }

    /**
     * restore scheduled transaction
     * @param ID
     * @param description
     * @param transaction
     * @param completed
     */
    public ScheduledTransaction(String ID, String description, ITransaction transaction, boolean completed) {
        this.ID = ID;
        this.description = description;
        this.transaction = transaction;
        this.completed = completed;
    }



    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ITransaction getTransaction() {
        return transaction;
    }

    @Override
    public ITransaction getTransaction(Date date) {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public String getID() {
        return ID;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
