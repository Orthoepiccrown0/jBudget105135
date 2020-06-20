package it.unicam.cs.pa.jbudget105135.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.interfaces.*;
import it.unicam.cs.pa.jbudget105135.model.*;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller implements IController {
    private final Gson GLedger;
    private final ILedger ledger;
    private final File saveFile;
    private boolean isOn;

    public Controller(ILedger ledger, File saveFile) {
        this.GLedger = new GsonBuilder().setPrettyPrinting().create();
        this.ledger = ledger;
        this.saveFile = saveFile;
        isOn = true;
    }

    @Override
    public void saveChanges() {
        FileManager fileManager = new FileManager();
        fileManager.save(GLedger.toJson(ledger), saveFile);
    }

    @Override
    public void addTransaction(ITransaction transaction) {
        ledger.addTransaction(transaction);
    }

    @Override
    public void addAccount(IAccount account) {
        ledger.addAccount(account);
    }

    @Override
    public void addScheduledTransaction(IScheduledTransaction transaction) {
        ledger.addScheduledTransaction(transaction);
    }

    @Override
    public void addTags(List<ITag> tags) {
        ledger.addTags(tags);
    }

    @Override
    public Gson getGLedger() {
        return GLedger;
    }

    @Override
    public ILedger getLedger() {
        return ledger;
    }

    @Override
    public File getSaveFile() {
        return saveFile;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public void close() {
        isOn = false;
    }

    @Override
    public boolean executeScheduledTransactions() {
        LocalDate now = LocalDate.now();
        boolean completed = false;
        for (IScheduledTransaction sTransaction : ledger.getScheduledTransaction()) {
            Date date = sTransaction.getTransaction().getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (now.isAfter(localDate) || now.isEqual(localDate)) {
                if (!ledger.getTransactions().contains(sTransaction.getTransaction())) {
                    ledger.addTransaction(sTransaction.getTransaction());
                    sTransaction.setCompleted(true);
                    completed = true;
                }
            }
        }
        return completed;
    }

    /**
     * transform list of Interfaces into implementing classes
     *
     * @param iMovements list of interfaces
     * @return implementing classes
     */
    public List<Movement> transformIMovements(List<IMovement> iMovements) {
        List<Movement> movements = new ArrayList<>();
        for (IMovement m : iMovements) {
            movements.add(new Movement(m.getID(),
                    m.getDescription(),
                    m.getAmount(),
                    m.getType(),
                    m.tags(),
                    m.getDate()));
        }
        return movements;
    }

    public List<Transaction> transformITransactions(List<ITransaction> iTransaction) {
        List<Transaction> transactions = new ArrayList<>();
        for (ITransaction m : iTransaction) {
            transactions.add(new Transaction(m.getID(),
                    m.getMovements(),
                    m.getTags(),
                    m.getDate(),
                    m.getName()));
        }
        return transactions;
    }

    public List<Account> transformIAccount(List<IAccount> iAccounts) {
        List<Account> acc = new ArrayList<>();
        for (IAccount m : iAccounts) {
            acc.add(new Account(m.getID(),
                    m.getType(),
                    m.getName(),
                    m.getDescription(),
                    m.getOpeningBalance(),
                    m.getMovements(),
                    m.getBalance()));
        }
        return acc;
    }

    public List<ScheduledTransaction> transformIScheduled(List<IScheduledTransaction> iScheduledTransactions) {
        List<ScheduledTransaction> tr = new ArrayList<>();
        for (IScheduledTransaction m : iScheduledTransactions) {
            tr.add(new ScheduledTransaction(m.getID(),
                    m.getDescription(),
                    m.getTransaction(),
                    m.isCompleted()
            ));
        }
        return tr;
    }

    /**
     * replacement of transaction(to save new name etc.)
     *
     * @param transaction transactionto be saved
     */
    public void searchTransactionAndReplaceIt(Transaction transaction) {
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Transaction t = (Transaction) ledger.getTransactions().get(i);
            if (transaction.getID().equals(t.getID()))
                ledger.getTransactions().set(i, transaction);
        }
    }

    /**
     * complete deleting of transaction
     *
     * @param transaction transaction to be deleted
     */
    public void searchTransactionDeleteIt(Transaction transaction) {
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Transaction t = (Transaction) ledger.getTransactions().get(i);
            if (transaction.getID().equals(t.getID())) {
                removeMovementsFromAccount(t.getMovements());
                ledger.removeTransaction(t);
            }
        }
    }

    /**
     * deleting of movements in account
     *
     * @param movements movements to be deleted
     */
    private void removeMovementsFromAccount(List<IMovement> movements) {
        for (IAccount acc : ledger.getAccounts()) {
            acc.getMovements().removeAll(movements);
        }
    }

    /**
     * search of account and its replacement
     *
     * @param account account to be replaced
     */
    public void searchAccountAndReplaceIt(Account account) {
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Account t = (Account) ledger.getAccounts().get(i);
            if (account.getID().equals(t.getID()))
                ledger.getAccounts().set(i, account);
        }
    }

    /**
     * searching movements by tag
     *
     * @param movements list of movements to search from
     * @param tags      tags to search for
     * @return
     */
    public List<IMovement> searchMovementsByTag(List<IMovement> movements, String... tags) {
        List<IMovement> movementsByTag = new ArrayList<>();
        for (String tag : tags) {
            for (IMovement movement : movements) {
                Tag t = new Tag(tag);
                if (movement.tags().contains(t))
                    movementsByTag.add(movement);
            }
        }
        return movementsByTag;
    }

    /**
     * searching of scheduled transactions by date
     *
     * @param scheduledTransactions list of all scheduled transactions
     * @param date                  selected date
     * @return list of scheduled transactions or empty list
     */
    public List<ScheduledTransaction> searchScheduledTransactionByDate(List<ScheduledTransaction> scheduledTransactions,
                                                                       LocalDate date) {
        if (date == null)
            return null;
        List<ScheduledTransaction> movementsByDate = new ArrayList<>();
        for (ScheduledTransaction transaction : scheduledTransactions) {
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.isEqual(date))
                movementsByDate.add(transaction);
        }
        return movementsByDate;
    }
}
