package it.unicam.cs.pa.jbudget105135.utils;

import it.unicam.cs.pa.jbudget105135.classes.Account;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
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

    public static void searchTransactionAndReplaceIt(Transaction transaction, ILedger ledger){
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Transaction t = (Transaction) ledger.getTransactions().get(i);
            if(transaction.getID().equals(t.getID()))
                ledger.getTransactions().set(i,transaction);
        }
    }

    public static void searchTransactionDeleteIt(Transaction transaction, ILedger ledger){
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Transaction t = (Transaction) ledger.getTransactions().get(i);
            if(transaction.getID().equals(t.getID())) {
                removeMovementsFromAccount(t.getMovements(), ledger);
                ledger.getTransactions().remove(t);
            }
        }
    }

    private static void removeMovementsFromAccount(List<IMovement> movements, ILedger ledger) {
        for (IAccount acc:ledger.getAccounts()) {
            acc.getMovements().removeAll(movements);
        }
    }

    public static void searchAccountAndReplaceIt(Account account, ILedger ledger){
        for (int i = 0; i < ledger.getTransactions().size(); i++) {
            Account t = (Account) ledger.getAccounts().get(i);
            if(account.getID().equals(t.getID()))
                ledger.getAccounts().set(i,account);
        }
    }

}
