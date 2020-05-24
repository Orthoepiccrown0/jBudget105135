package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Set;
import java.util.function.Consumer;

public interface ILedgerManager {

    void processCommand(String command);

    boolean isOn();

    Set<String> getCommandSet();

}