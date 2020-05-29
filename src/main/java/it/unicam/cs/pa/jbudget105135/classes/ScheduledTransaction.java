package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.IScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Date;
import java.util.List;

public class ScheduledTransaction implements ITransaction, IScheduledTransaction {
    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public List<ITransaction> getTransaction(Date date) {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<IMovement> getMovements() {
        return null;
    }

    @Override
    public void addMovement(IMovement movement) {

    }

    @Override
    public boolean removeMovement(IMovement movement) {
        return false;
    }

    @Override
    public List<ITag> getTags() {
        return null;
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
    public double getTotalAmount() {
        return 0;
    }

    @Override
    public Date getDate() {
        return null;
    }
}
