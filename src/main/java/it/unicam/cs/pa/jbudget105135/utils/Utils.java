package it.unicam.cs.pa.jbudget105135.utils;

import it.unicam.cs.pa.jbudget105135.classes.Account;
import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Movement> transformIMovements(List<IMovement> iMovements) {
        List<Movement> movements = new ArrayList<>();
        for (IMovement m : iMovements) {
            movements.add(new Movement(m.getID(),
                    m.getDescription(),
                    m.getAmount(),
                    m.getType(),
                    m.tags(),
                    null,
                    m.getAccount(),
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

}
