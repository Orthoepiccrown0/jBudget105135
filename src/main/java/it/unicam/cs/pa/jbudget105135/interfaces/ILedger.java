package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.AccountType;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ILedger {
    List<IAccount> getAccounts();

    boolean addTransaction(ITransaction transaction);

    boolean removeTransaction(ITransaction transaction);

    List<ITransaction> getTransactions();

    List<IScheduledTransaction> getScheduledTransaction();

    Set<ITag> getTags();

    boolean addAccount(AccountType accountType, String name, String description, double openingBalance);

    boolean addAccount(IAccount account);

    boolean addTag(String name, String description);

    boolean addTags(List<ITag> tags);

    boolean addScheduledTransaction(IScheduledTransaction scheduledTransaction);

    boolean isScheduled(Date date);
}
