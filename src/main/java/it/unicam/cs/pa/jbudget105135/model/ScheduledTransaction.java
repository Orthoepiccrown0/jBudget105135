package it.unicam.cs.pa.jbudget105135.model;

import it.unicam.cs.pa.jbudget105135.interfaces.IScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public class ScheduledTransaction implements IScheduledTransaction {

    private final String ID;
    private final String description;
    private final ITransaction transaction;
    private boolean completed;


    /**
     * Create new scheduled transaction
     *
     * @param description  description of transaction
     * @param transactions transaction
     */
    public ScheduledTransaction(String description, ITransaction transactions) {
        this.ID = UUID.randomUUID().toString();
        this.description = description;
        this.transaction = transactions;
        this.completed = false;
    }

    /**
     * restore scheduled transaction
     *
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
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String getID() {
        return ID;
    }

    public LocalDate getDate() {
        return transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
