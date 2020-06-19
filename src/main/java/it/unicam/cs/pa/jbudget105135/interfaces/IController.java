package it.unicam.cs.pa.jbudget105135.interfaces;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

public interface IController {

    /**
     * Controller state
     *
     * @return true if it is on
     */
    boolean isOn();

    /**
     * Close controller and set {@Code iOn=false}
     */
    void close();

    /**
     * Save all changes made
     */
    void saveChanges();

    /**
     * Add transaction
     *
     * @param transaction transaction to add
     */
    void addTransaction(ITransaction transaction);

    /**
     * Add account
     *
     * @param account account to add
     */
    void addAccount(IAccount account);

    /**
     * Add scheduled transaction
     *
     * @param transaction transaction to add
     */
    void addScheduledTransaction(IScheduledTransaction transaction);

    /**
     * Checks if there is any scheduled transaction to be completed in day of executing of app
     * and moving it to ledger transactions
     *
     * @param ledger ledger to check
     * @return true if there was at least one scheduled transaction
     */
    boolean executeScheduledTransactions(ILedger ledger);

    /**
     * Add tags
     *
     * @param tags list of tags to add
     */
    void addTags(List<ITag> tags);

    /**
     * Get ledger
     *
     * @return ledger
     */
    ILedger getLedger();

    /**
     * Get current file to save
     *
     * @return file
     */
    File getSaveFile();

    /**
     * Get current Gson
     *
     * @return Gson
     */
    Gson getGLedger();

}
