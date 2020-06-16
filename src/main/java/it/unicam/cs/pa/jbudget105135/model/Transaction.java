package it.unicam.cs.pa.jbudget105135.model;

import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Transaction implements ITransaction {

    private final String ID;
    private List<IMovement> movements;
    private List<ITag> tags;
    private Date date;
    private double totalAmount = 0;
    private String name;
    private final int numberOfMovements = 0;

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

    public void setTags(List<ITag> tags) {
        this.tags = tags;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
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
        totalAmount = 0;
        for (IMovement move : movements) {
            totalAmount += move.getAmount();
        }
    }

    public String getTagsString() {
        ArrayList<String> tagsStringList = new ArrayList<>();
        for (ITag tag : tags) {
            tagsStringList.add(tag.toString());
        }
        return String.join(",", tagsStringList);
    }

    public int getNumberOfMovements() {
        return movements.size();
    }

    public void setMovements(List<IMovement> movements) {
        this.movements = movements;
    }

    @Override
    public Date getDate() {
        return date;
    }

    public LocalDate getLocalDate() {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return ID.equals(that.ID) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
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
