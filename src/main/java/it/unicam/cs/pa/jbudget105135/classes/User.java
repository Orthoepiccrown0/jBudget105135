package it.unicam.cs.pa.jbudget105135.classes;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;

    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static User createNewUser(String nome, String email, String password) {
        User newUser = new User(UUID.randomUUID(), nome, email);
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


    public String getName() {
        return name;
    }


    public String getID() {
        return id.toString();
    }

}
