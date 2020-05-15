package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Date;
import java.util.List;

public interface IScheduledTransaction {
    String getDescription();

    List<ITransaction> getTransaction(Date date);

    boolean isCompleted();
}
