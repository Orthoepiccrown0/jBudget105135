package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.*;

public class Movement implements IMovement {

    private final String ID;
    private final String description;
    private final double amount;
    private final MovementType type;
    private final List<ITag> tags;
    private final Date date;

    //restore constructor
    public Movement(String ID, String description, double amount, MovementType type, List<ITag> tags,   Date date) {
        this.ID = ID;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.date = date;
    }

    //new movement
    public Movement(String description, double amount, MovementType type, List<ITag> tags,   Date date) {
        this.ID = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.date = date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public MovementType getType() {
        return type;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public List<ITag> tags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return ID.equals(movement.ID) &&
                description.equals(movement.description);
    }

    public String getTagsString(){
        ArrayList<String> tagsStringList = new ArrayList<>();
        for (ITag tag:tags) {
            tagsStringList.add(tag.toString());
        }
        return String.join(",",  tagsStringList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, description);
    }

    @Override
    public String toString() {
        return "Movement{" +
                "ID='" + ID + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", tags=" + Arrays.toString(tags.toArray()) +
                ", date=" + date +
                '}';
    }
}
