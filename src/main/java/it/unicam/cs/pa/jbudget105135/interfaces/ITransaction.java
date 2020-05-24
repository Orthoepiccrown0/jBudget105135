package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;

public interface ITransaction {
    String getID();

    List<IMovement> getMovements();

    void addMovement(IMovement movement);

    boolean removeMovement(IMovement movement);

    List<ITag> getTags();

    boolean addTag(ITag ITag);

    boolean removeTag(ITag ITag);

    double getTotalAmount();

    Date getDate();

}
