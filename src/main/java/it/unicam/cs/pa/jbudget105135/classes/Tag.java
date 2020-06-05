package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.Objects;
import java.util.UUID;

public class Tag implements ITag {

    private final String ID;
    private final String name;

    public Tag(String name) {
        this.name = name;
        this.ID = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return ID.equals(tag.ID) &&
                name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
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
