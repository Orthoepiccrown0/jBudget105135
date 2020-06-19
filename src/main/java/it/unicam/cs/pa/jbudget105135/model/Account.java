package it.unicam.cs.pa.jbudget105135.model;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class Account implements IAccount {

    private final String ID;
    private final AccountType type;
    private String name;
    private String description;
    private final double openingBalance;
    private final List<IMovement> movements;
    private double balance;

    /**
     * restore account
     */
    public Account(String ID, AccountType type, String name, String description, double openingBalance, List<IMovement> movements, double balance) {
        this.ID = ID;
        this.type = type;
        this.name = name;
        this.description = description;
        this.openingBalance = openingBalance;
        this.movements = movements;
        this.balance = balance;
    }

    /**
     * create new account
     */
    public Account(AccountType type, String name, String description, double openingBalance) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.openingBalance = openingBalance;
        this.balance = openingBalance;
        this.movements = new ArrayList<>();
        this.ID = UUID.randomUUID().toString();
    }

    @Override
    public void increaseBy(double amount) {
        balance += amount;
    }

    @Override
    public void decreaseBy(double amount) {
        balance -= amount;
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
    public double getBalance() {
        return balance;
    }

    @Override
    public List<IMovement> getMovements() {
        return movements;
    }

    @Override
    public List<IMovement> getMovements(Predicate<IMovement> predicate) {
        return null;
    }

    @Override
    public boolean addMovement(IMovement movement) {
        return movements.add(movement);
    }

    @Override
    public boolean removeMovement(IMovement movement) {
        return movements.remove(movement);
    }

    @Override
    public AccountType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ID.equals(account.ID) &&
                name.equals(account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + ", " + type + ", balance:" + balance;
    }
}
