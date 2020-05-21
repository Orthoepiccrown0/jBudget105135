package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class ConsoleLedgerManager<T extends ILedger> implements ILedgerManager {
    private HashMap<String, Consumer<? super ILedger>> commands;
    private boolean isOn;
    private T ledger;
    private BufferedReader reader;

    public ConsoleLedgerManager(T ledger, HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.ledger = ledger;
        this.isOn = true;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        addSimpleFunctions();
    }



    private void addSimpleFunctions() {
        this.commands.put("transactions", s -> showTransactions());
        this.commands.put("newtransaction", s -> createNewTransaction());
        this.commands.put("movements", s -> showMovements());
        this.commands.put("newmovement", s -> showTransactions());
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

    private void showTransactions() {
        if (ledger.getTransactions().size() != 0) {
            printTransactions(ledger.getTransactions());
        } else {
            System.out.println("There are no transactions!\nCreate new one!");
        }
    }

    private void printTransactions(List<ITransaction> transactions) {
        System.out.println("Transactions: ");
        for (ITransaction l : transactions) {
            System.out.println(l.toString());
        }
    }

    private void createNewTransaction() {
        List<ITag> tags;
        try {
            System.out.println("Welcome to transactions manager!");
            System.out.println("We are going to add a new transaction to this ledger.");
            System.out.println("First of all let's add some tags(use comma to use multiple tags):");
            String input = reader.readLine();
            if (input.contains(","))
                tags = generateTagsList(input.split(","));
            else
                tags = generateTagsList(new String[]{input});
            System.out.println("Now we need to add al least one movement.");
            Transaction transaction = new Transaction(new ArrayList<>(), tags, new Date());
            ledger.getTransactions().add(transaction);
            createMovement(transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<ITag> generateTagsList(String[] tags) {
        List<ITag> tagList = new ArrayList<>();
        for (String tag : tags) {
            if (!tag.equals(""))
                tagList.add(new Tag(tag));
        }
        return tagList;
    }

    private void showMovements() {
        try {
            System.out.println("Choose from which transaction we will whow you movements." +
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

    private void createMovement(Transaction transaction) {
        try {
            System.out.println("Welcome to movements manager.");
            System.out.println("Proceed instructions to create a new movement.");
            System.out.println("Please, insert description of movement:");
            String description = reader.readLine();
            System.out.println("Next, insert amount: ");
            double amount = Double.parseDouble(reader.readLine());
            System.out.println("Select movement type: ");
            List<MovementType> mTypes = Arrays.asList(MovementType.values());
            for (int i = 0; i < mTypes.size(); i++) {
                System.out.println((i + 1) + ") " + mTypes.get(i));
            }
            int selectedItem = Integer.parseInt(reader.readLine());
            MovementType selectedType = MovementType.CREDIT; //default
            if (selectedItem != 0)
                selectedType = mTypes.get(selectedItem - 1);
            System.out.println("Now insert some tags, use comma to split tags");
            String input = reader.readLine();
            List<ITag> tags;
            if (input.contains(","))
                tags = generateTagsList(input.split(","));
            else
                tags = generateTagsList(new String[]{input});
            Movement movement = new Movement(description, amount, selectedType, tags, transaction, null, new Date());
            transaction.addMovement(movement);
            System.out.println("Do you want to add another movement? [Y/N]");
            String response = reader.readLine();
            if(response.toLowerCase().equals("y"))
                createMovement(transaction);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
