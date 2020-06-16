package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.AccountType;

import java.util.List;
import java.util.function.Predicate;

public interface IAccount {
    /**
     * Name of account
     * @return returns name
     */
    String getName();

    /**
     * Description of account
     * @return returns description
     */
    String getDescription();

    /**
     * ID of account
     * @return returns ID
     */
    String getID();

    /**
     * Opening balance of account
     * @return returns opening balance
     */
    double getOpeningBalance();

    /**
     * Balance of account
     * @return returns current balance
     */
    double getBalance();

    /**
     * Increase current balance
     * @param amount amount to increase
     */
    void increaseBy(double amount);

    /**
     * Decrease current balance
     * @param amount amount to decrease
     */
    void decreaseBy(double amount);

    /**
     * Get list of movements associated with this account
     * @return list of movements
     */
    List<IMovement> getMovements();

    /**
     * Get list of movements associated with this account filtrate by predicate
     * @param predicate filter
     * @return list of movements
     */
    List<IMovement> getMovements(Predicate<IMovement> predicate);

    /**
     * Add movement to this account
     * @param movement movement to add
     * @return true if movement was added, false otherwise
     */
    boolean addMovement(IMovement movement);

    /**
     * Remove movement from this account
     * @param movement movement to remove
     * @return true if movement was removed, false otherwise
     */
    boolean removeMovement(IMovement movement);

    /**
     * Get account type
     * @return type
     */
    AccountType getType();
}
