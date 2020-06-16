package it.unicam.cs.pa.jbudget105135.utils;

import it.unicam.cs.pa.jbudget105135.interfaces.*;
import it.unicam.cs.pa.jbudget105135.model.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListUtils {

    /**
     * transform list of Interfaces into implementing classes
     *
     * @param iMovements list of interfaces
     * @return implementing classes
     */
    public static List<Movement> transformIMovements(List<IMovement> iMovements) {
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

    public static List<Transaction> transformITransactions(List<ITransaction> iTransaction) {
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

    public static List<Account> transformIAccount(List<IAccount> iAccounts) {
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

    public static List<ScheduledTransaction> transformIScheduled(List<IScheduledTransaction> iScheduledTransactions) {
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
     * @param ledger      current ledger
     */
    public static void searchTransactionAndReplaceIt(Transaction transaction, ILedger ledger) {
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
     * @param ledger      current ledger
     */
    public static void searchTransactionDeleteIt(Transaction transaction, ILedger ledger) {
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Transaction t = (Transaction) ledger.getTransactions().get(i);
            if (transaction.getID().equals(t.getID())) {
                removeMovementsFromAccount(t.getMovements(), ledger);
                ledger.getTransactions().remove(t);
            }
        }
    }

    /**
     * deleting of movements in account
     *
     * @param movements movements to be deleted
     * @param ledger    current ledger
     */
    private static void removeMovementsFromAccount(List<IMovement> movements, ILedger ledger) {
        for (IAccount acc : ledger.getAccounts()) {
            acc.getMovements().removeAll(movements);
        }
    }

    /**
     * search of account and its replacement
     *
     * @param account account to be replaced
     * @param ledger  current ledger
     */
    public static void searchAccountAndReplaceIt(Account account, ILedger ledger) {
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
    public static List<IMovement> searchMovementsByTag(List<IMovement> movements, String... tags) {
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
     * @param scheduledTransactions list of all scheduled transactions
     * @param date selected date
     * @return list of scheduled transactions or empty list
     */
    public static List<ScheduledTransaction> searchScheduledTransactionByDate(List<ScheduledTransaction> scheduledTransactions,
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

    /**
     * executing of scheduled transaction and moving it to ledger transactions
     *
     * @param ledger current ledger
     * @return true if any scheduled transaction were executed, false otherwise
     */
    public static boolean executeScheduledTransactions(ILedger ledger) {
        LocalDate now = LocalDate.now();
        for (IScheduledTransaction sTransaction : ledger.getScheduledTransaction()) {
            Date date = sTransaction.getTransaction().getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (now.isAfter(localDate) || now.isEqual(localDate)) {
                if (!ledger.getTransactions().contains(sTransaction.getTransaction())) {
                    ledger.addTransaction(sTransaction.getTransaction());
                    sTransaction.setCompleted(true);
                    return true;
                }
            }
        }
        return false;
    }
}
