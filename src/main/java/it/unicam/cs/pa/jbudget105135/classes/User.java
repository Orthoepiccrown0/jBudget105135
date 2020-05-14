package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;

import java.util.List;
import java.util.UUID;

public class User implements IAccount {
    private final UUID id;
    private final String name;
    private final String email;
    private final double openingBalance;

    private String description;

    public User(UUID id, String name, String email, double openingBalance, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.openingBalance = openingBalance;
        this.description = description;
    }

    public static User createNewUser(String nome, String email, double openingBalance, String description, String password) {
        User newUser = new User(UUID.randomUUID(), nome, email, openingBalance, description);
        //Create new user in database
        return newUser;
    }

    public static User login(String email, String password) {
        //login

        return null;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getID() {
        return id.toString();
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
