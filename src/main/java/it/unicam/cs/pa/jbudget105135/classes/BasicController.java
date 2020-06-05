package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.interfaces.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class BasicController<T extends ILedger> implements IController {
    private final HashMap<String, Consumer<? super ILedger>> commands;
    private final T ledger;
    private final BufferedReader reader;

    public BasicController(T ledger, HashMap<String, Consumer<? super ILedger>> commands) {
        this.commands = commands;
        this.ledger = ledger;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        addBasicFunctions();
    }

    private void addBasicFunctions() {
        this.commands.put("newtransaction", s -> createNewTransaction());
        this.commands.put("newmovement", s -> createNewMovement());
        this.commands.put("newaccount", s -> addAccount());
        this.commands.put("removetransaction", s -> removeTransaction());
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
    public Set<String> getCommandSet() {
        return commands.keySet();
    }

    private void removeTransaction() {
        try {
            System.out.println("Insert ID of transaction you want to remove or \"back\" to return:");
            String id = reader.readLine();
            if(id.equals("back"))
                return;
            ITransaction transaction = findTransaction(id);
            if(transaction!=null){
                for (IMovement m:transaction.getMovements()) {
                    for (IAccount acc:ledger.getAccounts()) {
                        acc.removeMovement(m);
                    }
                }
                ledger.removeTransaction(transaction);
                System.out.println("Transaction was successfully remove");
            }else{
                System.out.println("Unable to find transaction");
            }
        }catch (Exception e){
            System.out.println("Something went wrong");
        }
    }

    private void createNewMovement() {
        try {
            System.out.println("Insert ID of transaction where you want to insert new movement or \"back\" to return:");
            for (ITransaction t : ledger.getTransactions())
                System.out.println(t.toString());
            String id = reader.readLine();
            if (id.equals("back"))
                return;
            ITransaction transaction =  findTransaction(id);
            if (transaction == null) {
                System.out.println("Unable to find that transaction");
            } else {
                createMovement(transaction);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
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
            Transaction transaction = new Transaction(UUID.randomUUID().toString(),new ArrayList<>(), tags, new Date(),"name");
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


    private void createMovement(ITransaction transaction) {
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
            Movement movement = new Movement(description, amount, selectedType, tags,  transaction.getDate());
            if (account != null) {
                account.addMovement(movement);
            }
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
        } else if (type == MovementType.CREDIT) {
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
        return types[selectedType - 1];
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

    private ITransaction findTransaction(String transactionID) {
        for (ITransaction transaction : ledger.getTransactions()) {
            if (transaction.getID().equals(transactionID))
                return transaction;
        }
        return null;
    }
}
