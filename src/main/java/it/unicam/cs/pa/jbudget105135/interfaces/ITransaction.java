package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;

public interface ITransaction {

    /**
     * Get ID of transaction
     *
     * @return ID
     */
    String getID();

    /**
     * Get name of transaction
     *
     * @return name
     */
    String getName();

    /**
     * Get list of movements assigned to current transaction
     *
     * @return movements
     */
    List<IMovement> getMovements();

    /**
     * Add new movement
     *
     * @param movement new movement
     */
    void addMovement(IMovement movement);

    /**
     * Remove existing movement
     *
     * @param movement movement to remove
     * @return true if movements was removed, false otherwise
     */
    boolean removeMovement(IMovement movement);

    /**
     * Get list of tags used in all movements
     *
     * @return list of tags
     */
    List<ITag> getTags();

    /**
     * Add new tag
     *
     * @param ITag tag to add
     * @return true if tag was added, false otherwise
     */
    boolean addTag(ITag ITag);

    /**
     * Removes tag from list of tags
     *
     * @param ITag tag to remove
     * @return true if tag was removed, false otherwise
     */
    boolean removeTag(ITag ITag);

    /**
     * Get total amount of all movements
     *
     * @return total amount
     */
    double getTotalAmount();

    /**
     * Get date of transaction
     *
     * @return date
     */
    Date getDate();

}
