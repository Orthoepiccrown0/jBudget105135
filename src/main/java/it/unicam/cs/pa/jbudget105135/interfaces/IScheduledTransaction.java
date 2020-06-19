package it.unicam.cs.pa.jbudget105135.interfaces;

public interface IScheduledTransaction {

    /**
     * Get description if scheduled transaction
     *
     * @return description
     */
    String getDescription();

    /**
     * Get pending transaction
     *
     * @return transaction
     */
    ITransaction getTransaction();

    /**
     * Get state of transaction
     *
     * @return true if transaction is complete, false otherwise
     */
    boolean isCompleted();

    /**
     * Sets state of transaction
     *
     * @param completed new state
     */
    void setCompleted(boolean completed);

    /**
     * Get ID of scheduled transaction
     *
     * @return ID
     */
    String getID();
}
