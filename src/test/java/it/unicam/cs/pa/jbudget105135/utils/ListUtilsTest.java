package it.unicam.cs.pa.jbudget105135.utils;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.model.Ledger;
import it.unicam.cs.pa.jbudget105135.model.Movement;
import it.unicam.cs.pa.jbudget105135.model.Tag;
import it.unicam.cs.pa.jbudget105135.model.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ListUtilsTest extends TestCase {

    public void testSearchMovementsByTag() {
        Ledger ledger = new Ledger();
        List<ITag> movementTags = new ArrayList<>();
        List<ITag> transactionTags = new ArrayList<>();
        movementTags.add(new Tag("mtag1"));
        movementTags.add(new Tag("mtag2"));
        transactionTags.add(new Tag("ttag2"));
        transactionTags.add(new Tag("ttag2"));
        ITransaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), transactionTags, new Date(),"name");
        ledger.addAccount(AccountType.ASSETS, "name", "desc", 1500);
        IMovement movement = new Movement("description", 1000, MovementType.CREDIT, movementTags,  transaction.getDate());
        transaction.addMovement(movement);
        ledger.addTransaction(transaction);
        assertEquals(movement,ListUtils.searchMovementsByTag(transaction.getMovements(),"mtag1").get(0));
    }
}