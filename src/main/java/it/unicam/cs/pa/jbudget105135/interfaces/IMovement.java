package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.MovementType;

import java.util.Date;
import java.util.List;

public interface IMovement {

    /**
     * Get description of movement
     *
     * @return description
     */
    String getDescription();

    /**
     * Get type of movement
     *
     * @return type
     */
    MovementType getType();

    /**
     * Get amount of movement
     *
     * @return amount
     */
    double getAmount();

    /**
     * Get ID of movement
     *
     * @return ID
     */
    String getID();

    /**
     * Get Date of movement
     *
     * @return date
     */
    Date getDate();

    /**
     * Get list of  tags
     *
     * @return tags
     */
    List<ITag> tags();

}
