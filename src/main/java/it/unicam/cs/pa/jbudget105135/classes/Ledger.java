package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.util.Date;
import java.util.List;

public class Ledger implements ILedger {

    List<IAccount> accounts;


    @Override
    public List<IAccount> getAccounts() {
        return null;
    }

    @Override
    public boolean addTransaction(ITransaction ITransaction) {
        return false;
    }

    @Override
    public List<ITransaction> getTransactions() {
        return null;
    }

    @Override
    public List<ITag> getTags() {
        return null;
    }

    @Override
    public boolean addAccount(AccountType accountType, String name, String description, double openingBalance) {
        return false;
    }

    @Override
    public boolean addTag(String name, String description) {
        return false;
    }

    @Override
    public boolean addScheduledTransaction(IScheduledTransaction IScheduledTransaction) {
        return false;
    }

    @Override
    public boolean isScheduled(Date date) {
        return false;
    }
}
