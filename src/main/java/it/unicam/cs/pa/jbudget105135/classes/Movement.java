package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Movement implements IMovement {

    private final String ID;
    private final String description;
    private final double amount;
    private final MovementType type;
    private final List<ITag> tags;
    private final ITransaction transaction;
    private final IAccount account;
    private final Date date;

    //restore constructor
    public Movement(String ID, String description, double amount, MovementType type, List<ITag> tags, ITransaction transaction, IAccount account, Date date) {
        this.ID = ID;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.transaction = transaction;
        this.account = account;
        this.date = date;
    }

    //new movement
    public Movement(String description, double amount, MovementType type, List<ITag> tags, ITransaction transaction, IAccount account, Date date) {
        this.ID = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.transaction = transaction;
        this.account = account;
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
    public ITransaction getTransaction() {
        return transaction;
    }

    @Override
    public IAccount getAccount() {
        return account;
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
    public String toString() {
        return "Movement{" +
                "ID='" + ID + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", tags=" + Arrays.toString(tags.toArray()) +
                ", account=" + account +
                ", date=" + date +
                '}';
    }
}
