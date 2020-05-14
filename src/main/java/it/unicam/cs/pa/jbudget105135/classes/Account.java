package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;

import java.util.List;

public class Account implements IAccount {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public double getOpeningBalance() {
        return 0;
    }

    @Override
    public List<IMovement> getMovements() {
        return null;
    }
}
