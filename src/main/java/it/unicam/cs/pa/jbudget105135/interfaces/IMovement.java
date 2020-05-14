package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.MovementType;

import java.util.Date;
import java.util.List;

public interface IMovement {
    String getDescription();

    MovementType getType();

    double getAmount();

    ITransaction getTransaction();

    IAccount getAccount();

    String getID();

    Date getDate();

    List<ITag> tags();
}
