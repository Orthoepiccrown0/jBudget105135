package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;

public interface ITransaction {
    String getID();

    List<IMovement> getMovements();

    List<ITag> getTags();

    boolean addTag(ITag ITag);

    boolean removeTag(ITag ITag);

    Date getDate();

}