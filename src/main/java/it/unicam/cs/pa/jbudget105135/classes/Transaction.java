package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Transaction implements ITransaction {

    private final String ID;
    private final List<IMovement> movements;
    private final List<ITag> tags;
    private final Date date;
    private double totalAmount = 0;
    private final String name;

    /**
     * restore transaction
     *
     * @param ID
     * @param movements
     * @param tags
     * @param date
     */
    public Transaction(String ID, List<IMovement> movements, List<ITag> tags, Date date, String name) {
        this.ID = ID;
        this.movements = movements;
        this.tags = tags;
        this.date = date;
        this.name = name;
    }


    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
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
    public boolean removeMovement(IMovement movement) {
        return movements.remove(movement);
    }


    @Override
    public List<ITag> getTags() {
        return tags;
    }

    @Override
    public boolean addTag(ITag tag) {
        return tags.add(tag);
    }

    @Override
    public boolean removeTag(ITag tag) {
        return tags.remove(tag);
    }

    @Override
    public double getTotalAmount() {
        calculateTotalAmount();
        return totalAmount;
    }


    private void calculateTotalAmount() {
        for (IMovement move : movements) {
            totalAmount += move.getAmount();
        }
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "ID='" + ID + '\'' +
                ", tags=" + Arrays.toString(tags.toArray()) +
                ", totalAmount=" + getTotalAmount() +
                ", date=" + date +
                '}';
    }
}
