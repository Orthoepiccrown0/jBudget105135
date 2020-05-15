package it.unicam.cs.pa.jbudget105135;

import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;

public class App<T extends ILedger> {
    private boolean isOn;
    private T ledger;
    private final BufferedReader reader;
    private HashMap<String, Consumer<? super ILedger>> commands;

    public App(HashMap<String, Consumer<? super ILedger>> commands, T ledger) {
        this.ledger = ledger;
        this.isOn = true;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.commands.put("help", s -> printCommands());
        this.commands = commands;
    }

    private void printCommands() {
        TreeSet<String> words = new TreeSet<>(commands.keySet());
        String[] wordsArray = words.toArray(new String[]{});
        System.out.println("Commands: " + Arrays.toString(wordsArray));
    }

    public static void main(String[] args) throws IOException {
        createBasicLedger().start();
    }

    private void start() throws IOException {
        welcome();
        while (isOn) {
            System.out.print(" > ");
            System.out.flush();
            String command = reader.readLine();
            processCommand(command);

        }
        close();
    }

    private void processCommand(String command) {
    }

    private void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void welcome() {
        System.out.println("******************************");
        System.out.println("*         jBudget 0.1         *");
        System.out.println("******************************");
    }

    private static App createBasicLedger() {
        HashMap<String, Consumer<? super ILedger>> commands = new HashMap<>();
        addSimpleFunctions(commands);
        return new App(commands, new Ledger());
    }

    private static void addSimpleFunctions(HashMap<String, Consumer<? super ILedger>> commands) {

    }
}
