package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.AccountType;

import java.util.List;
import java.util.function.Predicate;

public interface IAccount {
    String getName();

    String getDescription();

    String getID();

    double getOpeningBalance();

    double getBalance();

    void increaseBy(double amount);

    void decreaseBy(double amount);

    List<IMovement> getMovements();

    List<IMovement> getMovements(Predicate<IMovement> predicate);

    boolean addMovement(IMovement movement);

    AccountType getType();
}
