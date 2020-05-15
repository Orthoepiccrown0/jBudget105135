package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.UUID;

public class Tag implements ITag {

    private final String ID;
    private final String name;
    private final String description;

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
        this.ID = UUID.randomUUID().toString();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return ID;
    }
}
