package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedgerManager;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;

public class LedgerManager<T extends ILedger> implements ILedgerManager {
    private HashMap<String, Consumer<? super ILedger>> commands;
    private boolean isOn;
    private T ledger;

    public LedgerManager(T ledger, HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.ledger = ledger;
        this.isOn = true;
    }

    @Override
    public void processCommand(String command) {
        Consumer<? super T> action = commands.get(command);
        if (action == null) {
            System.err.println("Unknown command: " + command);
        } else {
            action.accept(ledger);
        }
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public Set<String> getCommandSet() {
        return commands.keySet();
    }
}
