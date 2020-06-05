package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;

public class ConsoleIView<T extends ILedger> implements IView {

    private final BufferedReader reader;
    private final HashMap<String, Consumer<? super ILedger>> commands;
    private final ILedger ledger;
    private boolean isOn;

    public ConsoleIView(ILedger ledger, HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.ledger = ledger;
        this.isOn = true;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.commands.put("help", s -> printCommands());
        this.commands.put("accounts", s -> showAccounts());
        this.commands.put("movements", s -> showMovements());
        this.commands.put("transactions", s -> showTransactions());
        this.commands.put("exit", s -> exit());
    }

    private void exit() {
        isOn = false;
    }

    public void printCommands() {
        TreeSet<String> words = new TreeSet<>(commands.keySet());
        String[] wordsArray = words.toArray(new String[]{});
        System.out.println("Commands: " + Arrays.toString(wordsArray));
    }

    @Override
    public void open(IController ledgerManager) throws IOException {
        welcome();
        printCommands();
        while (isOn) {
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

    private void showAccounts() {
        if (ledger.getAccounts().size() == 0)
            System.out.println("No accounts available, create one!");
        for (IAccount account : ledger.getAccounts()) {
            System.out.println(account);
        }
    }

    private void showMovements() {
        try {
            if (ledger.getTransactions().size() == 0) {
                System.out.println("No transactions found. You can't access to movements without transaction.");
                return;
            }
            System.out.println("Choose from which transaction we will show you movements." +
                    " \nPlease insert transaction ID or \"back\" to return back: ");
            String transactionCode = reader.readLine();

            if (transactionCode.equals("back"))
                return;

            ITransaction transaction = findTransaction(transactionCode);
            if (transaction != null) {
                System.out.println("Movements of transaction with ID: " + transactionCode);
                printMovements(transaction.getMovements());
            } else
                System.out.println("Unable to find that transaction, returning to main menu..");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void printMovements(List<IMovement> movements) {
        System.out.println("Movements: ");
        for (IMovement movement : movements) {
            System.out.println(movement.toString());
        }
    }

    private ITransaction findTransaction(String transactionID) {
        for (ITransaction transaction : ledger.getTransactions()) {
            if (transaction.getID().equals(transactionID))
                return transaction;
        }
        return null;
    }

    private void showTransactions() {
        if (ledger.getTransactions().size() != 0) {
            printTransactions(ledger.getTransactions());
        } else {
            System.out.println("There are no transactions. Create new one!");
        }
    }

    private void printTransactions(List<ITransaction> transactions) {
        System.out.println("Transactions: ");
        for (ITransaction l : transactions) {
            System.out.println(l.toString());
        }
    }

}
