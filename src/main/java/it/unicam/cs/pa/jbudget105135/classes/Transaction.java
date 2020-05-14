package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Date;
import java.util.List;

public class Transaction implements ITransaction {

    private String ID;
    private List<IMovement> movements;
    private List<ITag> tags;
    private Date date;

    public Transaction(String ID, List<IMovement> movements, List<ITag> tags, Date date) {
        this.ID = ID;
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
}
