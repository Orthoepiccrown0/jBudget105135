package it.unicam.cs.pa.jbudget105135.interfaces;

public interface IScheduledTransaction {
    String getDescription();


    ITransaction getTransaction();

    boolean isCompleted();

    void setCompleted(boolean completed);

    String getID();
}
