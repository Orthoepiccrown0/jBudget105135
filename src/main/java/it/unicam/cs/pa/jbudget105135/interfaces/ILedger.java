package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ILedger {
    /**
     * Get list of accounts
     *
     * @return list of accounts
     */
    List<IAccount> getAccounts();

    /**
     * Add transaction to ledger
     *
     * @param transaction to add
     * @return true if transactions was added, false otherwise
     */
    boolean addTransaction(ITransaction transaction);

    /**
     * Remove transaction and its movements from ledger
     *
     * @param transaction
     * @return
     */
    boolean removeTransaction(ITransaction transaction);

    /**
     * Get list of transactions
     *
     * @return
     */
    List<ITransaction> getTransactions();

    /**
     * Get list of Scheduled transactions
     *
     * @return list of scheduled transactions
     */
    List<IScheduledTransaction> getScheduledTransaction();

    /**
     * Get set of tags
     *
     * @return set of tags
     */
    Set<ITag> getTags();

    /**
     * Add account to ledger
     *
     * @param account account to add
     * @return true if account was added, false otherwise
     */
    boolean addAccount(IAccount account);

    /**
     * Add new tag to ledger
     *
     * @param name        name of time
     * @param description description of tag
     * @return true if tag was added, false otherwise
     */
    boolean addTag(String name, String description);

    /**
     * Add list of tags into ledger
     *
     * @param tags
     * @return true if tag were added, false otherwise
     */
    boolean addTags(List<ITag> tags);

    /**
     * Add new scheduled transaction to ledger
     *
     * @param scheduledTransaction transaction to add
     * @return true if transaction was added, false otherwise
     */
    boolean addScheduledTransaction(IScheduledTransaction scheduledTransaction);

    /**
     * Looks if on chosen date there is a scheduled transaction
     *
     * @param date date to check
     * @return true if there is a scheduled transaction, false otherwise
     */
    boolean isScheduled(Date date);
}
