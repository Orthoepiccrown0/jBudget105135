package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class ConsoleIView<T extends ILedger> implements IView {

    private final BufferedReader reader;
    private final HashMap<String, Consumer<? super ILedger>> commands;

    public ConsoleIView(HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.commands.put("help", s -> printCommands());
    }

    public void printCommands() {
        TreeSet<String> words = new TreeSet<>(commands.keySet());
        String[] wordsArray = words.toArray(new String[]{});
        System.out.println("Commands: " + Arrays.toString(wordsArray));
    }


    @Override
    public void open(ILedgerManager ledgerManager) throws IOException {
        welcome();
        printCommands();
        while (ledgerManager.isOn()) {
            System.out.print("> ");
            System.out.flush();
            String command = reader.readLine();
            ledgerManager.processCommand(command);

        }
        close();
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            goodbye();
        }
    }

    private void goodbye() {
        System.out.println("******************************");
    }

    private void welcome() {
        System.out.println("******************************");
        System.out.println("*         jBudget 0.1         *");
        System.out.println("******************************");
    }

}
