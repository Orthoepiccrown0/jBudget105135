package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Ledger implements ILedger {

    private final List<IAccount> accounts;
    private final List<ITransaction> transactions;
    private final List<IScheduledTransaction> scheduledTransactions;
    private final List<ITag> tags;
    private String ID;


    /**
     * restore ledger
     *
     * @param accounts
     * @param transactions
     * @param scheduledTransactions
     * @param tags
     * @param ID
     */


    /**
     * create new ledger
     */
    public Ledger() {
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.scheduledTransactions = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.ID = UUID.randomUUID().toString();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public List<IAccount> getAccounts() {
        return accounts;
    }

    @Override
    public boolean addTransaction(ITransaction transaction) {
        return transactions.add(transaction);
    }

    @Override
    public boolean removeTransaction(ITransaction transaction) {
        return transactions.remove(transaction);
    }

    @Override
    public List<ITransaction> getTransactions() {
        return transactions;
    }

    @Override
    public List<ITag> getTags() {
        return tags;
    }

    @Override
    public boolean addAccount(AccountType accountType, String name, String description, double openingBalance) {
        return accounts.add(new Account(accountType, name, description, openingBalance));
    }

    @Override
    public boolean addTag(String name, String description) {
        return tags.add(new Tag(name));
    }

    @Override
    public boolean addScheduledTransaction(IScheduledTransaction scheduledTransaction) {
        return scheduledTransactions.add(scheduledTransaction);
    }

    @Override
    public boolean isScheduled(Date date) {
        return false;
    }
}
