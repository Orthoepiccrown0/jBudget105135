package it.unicam.cs.pa.jbudget105135.model;

import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.util.*;

public class Ledger implements ILedger {

    private final List<IAccount> accounts;
    private final List<ITransaction> transactions;
    private final List<IScheduledTransaction> scheduledTransactions;
    private final Set<ITag> tags;
    private final String ID;

    /**
     * create new ledger
     */
    public Ledger() {
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.scheduledTransactions = new ArrayList<>();
        this.tags = new HashSet<>();
        this.ID = UUID.randomUUID().toString();
    }

    public String getID() {
        return ID;
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
    public List<IScheduledTransaction> getScheduledTransaction() {
        return scheduledTransactions;
    }

    @Override
    public Set<ITag> getTags() {
        return tags;
    }

    @Override
    public boolean addAccount(IAccount account) {
        return accounts.add(account);
    }

    @Override
    public boolean addTag(String name, String description) {
        return tags.add(new Tag(name));
    }

    @Override
    public boolean addTags(List<ITag> tags) {
        return this.tags.addAll(tags);
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
