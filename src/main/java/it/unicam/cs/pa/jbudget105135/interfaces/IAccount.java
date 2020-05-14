package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.List;

public interface IAccount {
    String getName();

    String getDescription();

    String getID();

    double getOpeningBalance();

    List<IMovement> getMovements();
}
