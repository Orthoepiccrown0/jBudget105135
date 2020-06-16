package it.unicam.cs.pa.jbudget105135.model;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionTest extends TestCase {


    public void testGetMovements() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag2"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags, transaction.getDate());
        transaction.addMovement(movement);
        assertEquals(movement.getID(), transaction.getMovements().get(0).getID());
    }


    public void testRemoveMovement() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag2"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,   transaction.getDate());
        transaction.addMovement(movement);
        transaction.removeMovement(movement);
        assertEquals(0, transaction.getMovements().size());
    }

    public void testGetTags() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag1"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,   transaction.getDate());
        transaction.addMovement(movement);
        assertEquals("ttag1", transaction.getTags().get(0).toString());
        assertEquals("ttag2", transaction.getTags().get(1).toString());
    }

    public void testAddTag() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag1"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,   transaction.getDate());
        transaction.addMovement(movement);
        transaction.addTag(new Tag("ttag3"));
        assertEquals("ttag1", transaction.getTags().get(0).toString());
        assertEquals("ttag2", transaction.getTags().get(1).toString());
        assertEquals("ttag3", transaction.getTags().get(2).toString());
    }

    public void testRemoveTag() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag1"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,   transaction.getDate());
        transaction.addMovement(movement);
        transaction.removeTag(transaction.getTags().get(0));
        assertEquals(1, transaction.getTags().size());
    }

    public void testGetTotalAmount() {
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag1"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,   transaction.getDate());
        IMovement movement1 = new Movement("description", 500, MovementType.DEBIT, movementTags,   transaction.getDate());
        transaction.addMovement(movement);
        transaction.addMovement(movement1);
        assertEquals(1500.0,transaction.getTotalAmount());
    }
}