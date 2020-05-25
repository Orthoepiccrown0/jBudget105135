package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.AccountType;

import java.util.Date;
import java.util.List;

public interface ILedger {
    List<IAccount> getAccounts();

    boolean addTransaction(ITransaction transaction);

    boolean removeTransaction(ITransaction transaction);

    List<ITransaction> getTransactions();

    List<ITag> getTags();

    boolean addAccount(AccountType accountType, String name, String description, double openingBalance);

    boolean addTag(String name, String description);

    boolean addScheduledTransaction(IScheduledTransaction scheduledTransaction);

    boolean isScheduled(Date date);
}
