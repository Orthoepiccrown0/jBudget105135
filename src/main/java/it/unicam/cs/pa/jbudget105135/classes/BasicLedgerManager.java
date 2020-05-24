package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class BasicLedgerManager<T extends ILedger> implements ILedgerManager {
    private final HashMap<String, Consumer<? super ILedger>> commands;
    private final T ledger;
    private final BufferedReader reader;
    private boolean isOn;

    public BasicLedgerManager(T ledger, HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.ledger = ledger;
        this.isOn = true;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.commands.put("exit", s -> exit());
        addSimpleFunctions();
    }

    private void exit() {
        isOn = false;
    }


    private void addSimpleFunctions() {
        this.commands.put("transactions", s -> showTransactions());
        this.commands.put("newtransaction", s -> createNewTransaction());
        this.commands.put("movements", s -> showMovements());
        this.commands.put("newmovement", s -> showTransactions());
        this.commands.put("accounts", s -> showAccounts());
        this.commands.put("newaccount", s -> addAccount());
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
            System.out.println("There are no transactions. Create new one!");
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
            createMovement(transaction);
            if (transaction.getMovements().size() == 0)
                System.out.println("No transaction was added");
            else
                ledger.addTransaction(transaction);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<ITag> generateTagsList(String[] tags) {
        List<ITag> tagList = new ArrayList<>();
        for (String tag : tags) {
            if (!tag.equals(""))
                tagList.add(new Tag(tag.trim()));
        }
        return tagList;
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

    private void createMovement(Transaction transaction) {
        try {
            //check if list of accounts is not empty
            if (ledger.getAccounts().size() == 0)
                requestAccount();

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

            IAccount account = chooseAccountAndCheckAffordable(selectedType, amount);
            Movement movement = new Movement(description, amount, selectedType, tags, transaction, account, transaction.getDate());
            transaction.addMovement(movement);
            System.out.println("Do you want to add another movement? [Y/N]");
            String response = reader.readLine();
            if (response.toLowerCase().equals("y"))
                createMovement(transaction);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong, restarting..");
            createMovement(transaction);
        }
    }

    private IAccount chooseAccountAndCheckAffordable(MovementType type, double amount) throws IOException {
        System.out.println("Choose an account from the list:");
        for (int i = 0; i < ledger.getAccounts().size(); i++) {
            IAccount ac = ledger.getAccounts().get(i);
            System.out.println((i + 1) + ") " + ac.getName() + " " + ac.getType() + " Balance: " + ac.getBalance());
        }
        int selectedAccount = Integer.parseInt(reader.readLine());
        IAccount account = ledger.getAccounts().get(selectedAccount - 1);
        if (type == MovementType.DEBIT) {
            if (account.getType() == AccountType.ASSETS) {
                if (account.getBalance() < amount) {
                    System.out.println("You cant afford this movement");
                    return null;
                } else {
                    account.decreaseBy(amount);
                }
            } else if (account.getType() == AccountType.LIABILITIES) {
                account.increaseBy(amount);
            }
        }else if(type == MovementType.CREDIT){
            if (account.getType() == AccountType.ASSETS) {
                account.increaseBy(amount);
            } else if (account.getType() == AccountType.LIABILITIES) {
                if (account.getBalance() < amount) {
                    System.out.println("You cant afford this movement");
                    return null;
                } else {
                    account.decreaseBy(amount);
                }
            }
        }

        return account;
    }

    private void requestAccount() {
        try {
            System.out.println("Wait! You dont have any account so you should create at least one.");
            System.out.println("First of all you need to choose an account type:");
            createNewAccount();
        } catch (Exception ex) {
            System.out.println("Something went wrong, restarting");
            requestAccount();
        }

    }

    private void createNewAccount() throws IOException {
        AccountType type = selectAccountType();
        System.out.println("Next insert name of account:");
        String name = reader.readLine();
        System.out.println("Insert a description:");
        String description = reader.readLine();
        System.out.println("Insert opening balance:");
        double amount = Double.parseDouble(reader.readLine());
        ledger.addAccount(type, name, description, amount);
    }

    private AccountType selectAccountType() {
        int indicator = 1;
        AccountType[] types = AccountType.values();
        for (AccountType type : types) {
            System.out.println(indicator + ") " + type);
            indicator++;
        }
        int selectedType = 0;
        try {
            selectedType = Integer.parseInt(reader.readLine());
            if (selectedType == 0 || selectedType > types.length)
                throw new IOException();
        } catch (IOException ioException) {
            System.out.println("Please, insert a valid number");
            return selectAccountType();
        }
        return types[selectedType-1];
    }

    private void showAccounts() {
        if (ledger.getAccounts().size() == 0)
            System.out.println("No accounts available, create one!");
        for (IAccount account : ledger.getAccounts()) {
            System.out.println(account);
        }
    }

    private void addAccount() {
        System.out.println("Now we are going to create a new account.");
        try {
            createNewAccount();
        } catch (IOException ioException) {
            System.out.println("Something went wrong, restarting");
            addAccount();
        }
    }
}
