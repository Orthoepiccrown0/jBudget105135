package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.Set;
import java.util.function.Consumer;

public interface IController {

    void processCommand(String command);

    Set<String> getCommandSet();

}
