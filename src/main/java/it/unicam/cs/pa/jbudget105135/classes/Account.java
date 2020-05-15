package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;

import java.util.List;
import java.util.UUID;

public class Account implements IAccount {

    private final String ID;
    private final AccountType type;
    private final String name;
    private final String description;
    private final double openingBalance;
    private List<IMovement> movements;

    /**
     * restore account
     *
     * @param ID
     * @param type
     * @param name
     * @param description
     * @param openingBalance
     * @param movements
     */
    public Account(String ID, AccountType type, String name, String description, double openingBalance, List<IMovement> movements) {
        this.ID = ID;
        this.type = type;
        this.name = name;
        this.description = description;
        this.openingBalance = openingBalance;
        this.movements = movements;
    }

    /**
     * create new account
     *
     * @param type
     * @param name
     * @param description
     * @param openingBalance
     */
    public Account(AccountType type, String name, String description, double openingBalance) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.openingBalance = openingBalance;
        this.ID = UUID.randomUUID().toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public double getOpeningBalance() {
        return openingBalance;
    }

    @Override
    public List<IMovement> getMovements() {
        return movements;
    }

    @Override
    public boolean addMovement(IMovement movement) {
        return movements.add(movement);
    }

    @Override
    public AccountType getType() {
        return type;
    }

}
