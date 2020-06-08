package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;

public interface IScheduledTransaction {
    String getDescription();

    ITransaction getTransaction(Date date);

    ITransaction getTransaction();

    boolean isCompleted();

    void setCompleted(boolean completed);

    String getID();
}
