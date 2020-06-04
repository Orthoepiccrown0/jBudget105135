package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.UUID;

public class Tag implements ITag {

    private final String ID;
    private final String name;

    public Tag(String name) {
        this.name = name;
        this.ID = UUID.randomUUID().toString();
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return name.trim();
    }
}
