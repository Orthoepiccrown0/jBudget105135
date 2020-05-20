package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Transaction implements ITransaction {

    private final String ID;
    private final List<IMovement> movements;
    private final List<ITag> tags;
    private final Date date;

    public Transaction(String ID, List<IMovement> movements, List<ITag> tags, Date date) {
        this.ID = ID;
        this.movements = movements;
        this.tags = tags;
        this.date = date;
    }

    public Transaction(List<IMovement> movements, List<ITag> tags, Date date) {
        this.ID = UUID.randomUUID().toString();
        this.movements = movements;
        this.tags = tags;
        this.date = date;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public List<IMovement> getMovements() {
        return movements;
    }

    @Override
    public void addMovement(IMovement movement) {
        movements.add(movement);
    }



    @Override
    public List<ITag> getTags() {
        return tags;
    }

    @Override
    public boolean addTag(ITag ITag) {
        return false;
    }

    @Override
    public boolean removeTag(ITag ITag) {
        return false;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID='" + ID + '\'' +
                ", tags=" + tags +
                ", date=" + date +
                '}';
    }
}
