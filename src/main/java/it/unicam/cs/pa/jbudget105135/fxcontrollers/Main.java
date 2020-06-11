package it.unicam.cs.pa.jbudget105135.fxcontrollers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.classes.*;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.utils.FileManager;
import it.unicam.cs.pa.jbudget105135.utils.ListUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The class  {@code Main} contains methods that are used to display data
 * and interact with it
 */
public class Main implements Initializable {
    public Pane controlsPane;
    public Pane transactionsBPane;
    public Pane accountsBPane;
    public Pane tagsBPane;
    public Pane loadBPane;
    public Pane newBPane;
    public Pane scheduledTransactionsBPane;
    public Pane searchPane;
    public Pane datePane;

    public Button firstActionButton;
    public Button secondActionButton;
    public Button thirdActionButton;

    public Label page;

    public TableView<Transaction> transactionsTable;
    public TableView<Account> accountsTable;
    public TableView<ScheduledTransaction> scheduledTable;
    public TableView<Movement> tagsTable;

    public ProgressBar progressBar;
    public AnchorPane noFileSelectedPane;
    public TextField searchField;
    public DatePicker dateField;

    private Gson GLedger = new GsonBuilder().setPrettyPrinting().create();
    private ILedger ledger;
    private File saveFile;

    ArrayList<Transaction> transactions;
    ArrayList<Account> accounts;
    ArrayList<ScheduledTransaction> scheduledTransactions;




    private enum PageType {
        TRANSACTIONS,
        TAGS,
        SCHEDULED_TRANSACTION,
        MOVEMENTS,  //for future implementation
        ACCOUNTS
    }

    private PageType currentPage;
    private Transaction selectedTransaction;
    private Account selectedAccount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlsPane.setVisible(false);
        lockMenu();
        setupTableForScheduledTransactions();
        setupTableForTransactions();
        setupTableForAccounts();
        setupTableForTags();
    }

    /**
     * search movements by tag
     */
    public void searchByTag() {
        if (searchField.getText().trim().length() > 0) {
            List<IMovement> tmpMovements = new ArrayList<>();
            for (IAccount account : ledger.getAccounts())
                tmpMovements.addAll(account.getMovements());

            List<IMovement> movementsContainingTags = ListUtils.searchMovementsByTag(tmpMovements, searchField.getText().trim());
            tagsTable.getItems().clear();
            tagsTable.getItems().addAll(ListUtils.transformIMovements(movementsContainingTags));
        }
    }

    /**
     * search scheduled transaction by date
     */
    public void searchByDate() {
        List<ScheduledTransaction> transactions =
                ListUtils.searchScheduledTransactionByDate(scheduledTransactions,dateField.getValue());
        if(transactions==null)
            return;
        scheduledTable.getItems().clear();
        scheduledTable.getItems().addAll(transactions);
    }

    /**
     * cancel search query
     */
    public void clearSearchByDate() {
        dateField.setValue(null);
        scheduledTable.getItems().clear();
        scheduledTable.getItems().addAll(scheduledTransactions);
    }

    /**
     * Switch current tab to transactions tab
     */
    public void switchToTransactions() {
        currentPage = PageType.TRANSACTIONS;
        page.setVisible(true);
        page.setText("Transactions");
        accountsTable.setVisible(false);
        transactionsTable.setVisible(true);
        tagsTable.setVisible(false);
        scheduledTable.setVisible(false);
        setupActionButtonsForTransactions();

    }

    /**
     * Switch current tab to accounts tab
     */
    public void switchToAccounts() {
        currentPage = PageType.ACCOUNTS;
        page.setVisible(true);
        page.setText("Accounts");
        accountsTable.setVisible(true);
        transactionsTable.setVisible(false);
        tagsTable.setVisible(false);
        scheduledTable.setVisible(false);
        setupActionButtonsForAccounts();

    }

    /**
     * Switch current tab to search by tags tab
     */
    public void switchToTags() {
        currentPage = PageType.TAGS;
        page.setVisible(true);
        page.setText("Movements by Tags");
        accountsTable.setVisible(false);
        transactionsTable.setVisible(false);
        tagsTable.setVisible(true);
        scheduledTable.setVisible(false);
        hideActionButtonsAndShowSearch();
    }

    /**
     * Switch current tab to ScheduledTransactions tab
     */
    public void switchToScheduledTransactions() {
        currentPage = PageType.SCHEDULED_TRANSACTION;
        page.setVisible(true);
        page.setText("Scheduled transactions");
        accountsTable.setVisible(false);
        transactionsTable.setVisible(false);
        tagsTable.setVisible(false);
        scheduledTable.setVisible(true);
        setupActionButtonsForScheduledTransactions();
    }

    /**
     * creating of new file in specific directory by using FileChooser
     */
    public void createNewFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save jBudget file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        saveFile = fileChooser.showSaveDialog(transactionsBPane.getScene().getWindow());
        if (saveFile != null) {
            FileManager.saveToFile(saveFile.getPath(), this, "");
            ledger = new Ledger();
            noFileSelectedPane.setVisible(false);
            unlockMenu();
            switchToAccounts();
            addNewAccount();
        }

    }

    /**
     * loading from chosen file on mouse click
     */
    public void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open jBudget .json file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        saveFile = fileChooser.showOpenDialog(transactionsBPane.getScene().getWindow());
        if (saveFile != null) {
            FileManager.loadDataFromFile(saveFile.getPath(), this);
            noFileSelectedPane.setVisible(false);
            unlockMenu();
            switchToTransactions();
        }
    }

    private void unlockMenu() {
        transactionsBPane.setDisable(false);
        accountsBPane.setDisable(false);
        scheduledTransactionsBPane.setDisable(false);
        tagsBPane.setDisable(false);
    }

    private void lockMenu() {
        transactionsBPane.setDisable(true);
        accountsBPane.setDisable(true);
        scheduledTransactionsBPane.setDisable(true);
        tagsBPane.setDisable(true);
    }

    public void firstAction() {
        if (currentPage == PageType.TRANSACTIONS) {
            addNewTransaction();
        } else if (currentPage == PageType.ACCOUNTS) {
            addNewAccount();
        } else if (currentPage == PageType.SCHEDULED_TRANSACTION) {
            addNewScheduledTransaction();
        }
    }


    public void secondAction() {
        if (currentPage == PageType.TRANSACTIONS) {
            deleteSelectedItem();
        } else if (currentPage == PageType.ACCOUNTS) {
            deleteSelectedItem();
        }
    }

    private void deleteSelectedItem() {
        if (currentPage == PageType.TRANSACTIONS) {
            if (selectedTransaction != null) {
                ListUtils.searchTransactionDeleteIt(selectedTransaction, ledger);
                refreshTransactionsTable();
            }
        } else if (currentPage == PageType.ACCOUNTS) {
            //todo: figure out if deleting of account is needed
        }
        saveChanges();
    }

    public void thirdAction() {
        //future implementations
    }

    private void setupActionButtonsForTransactions() {
        setDeleteAndAddButtons();
    }

    private void setupActionButtonsForAccounts() {
        setDeleteAndAddButtons();
    }

    private void setupActionButtonsForScheduledTransactions() {
        setDeleteAndAddButtons();
        showSearchByDate();
    }

    private void setDeleteAndAddButtons() {
        searchPane.setVisible(false);
        datePane.setVisible(false);
        controlsPane.setVisible(true);
        thirdActionButton.setVisible(false);
        secondActionButton.setDisable(true);
        secondActionButton.setText("Delete");
        secondActionButton.getStyleClass().removeAll();
        secondActionButton.getStyleClass().add("redButton");
        firstActionButton.setText("Add");
        firstActionButton.getStyleClass().removeAll();
        firstActionButton.getStyleClass().add("greenButton");
    }

    /**
     * show search by tags
     */
    private void hideActionButtonsAndShowSearch() {
        controlsPane.setVisible(false);
        searchPane.setVisible(true);
        datePane.setVisible(false);
    }

    /**
     * show search by date
     */
    private void showSearchByDate() {
        searchPane.setVisible(false);
        datePane.setVisible(true);
    }

    private void setupTableForAccounts() {
        TableColumn<Account, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Account, Double> column2 = new TableColumn<>("Balance");
        column2.setCellValueFactory(new PropertyValueFactory<>("balance"));
        TableColumn<Account, AccountType> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Account, String> column4 = new TableColumn<>("Description");
        column4.setCellValueFactory(new PropertyValueFactory<>("description"));
        accountsTable.getColumns().addAll(column1, column2, column3, column4);
        TableView.TableViewSelectionModel<Account> selectionModel = accountsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setAccountsTableClickListeners();
    }

    private void setupTableForTransactions() {
        TableColumn<Transaction, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Transaction, Integer> column2 = new TableColumn<>("N. of movements");
        column2.setCellValueFactory(new PropertyValueFactory<>("numberOfMovements"));
        TableColumn<Transaction, Double> column3 = new TableColumn<>("Total");
        column3.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        TableColumn<Transaction, Date> column4 = new TableColumn<>("Date");
        column4.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Transaction, String> column5 = new TableColumn<>("Tags");
        column5.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        transactionsTable.getColumns().addAll(column1, column2, column3, column4, column5);
        TableView.TableViewSelectionModel<Transaction> selectionModel = transactionsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setTransactionClickListeners();
    }

    private void setupTableForTags() {
        TableColumn<Movement, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movement, Double> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Movement, MovementType> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Movement, String> column4 = new TableColumn<>("Tags");
        column4.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        tagsTable.getColumns().addAll(column1, column2, column3, column4);
        TableView.TableViewSelectionModel<Movement> selectionModel = tagsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    private void setupTableForScheduledTransactions() {
        TableColumn<ScheduledTransaction, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<ScheduledTransaction, Boolean> column2 = new TableColumn<>("Completed");
        column2.setCellValueFactory(new PropertyValueFactory<>("completed"));
        TableColumn<ScheduledTransaction, Boolean> column3 = new TableColumn<>("Date");
        column3.setCellValueFactory(new PropertyValueFactory<>("date"));
        column1.setMinWidth(100);
        scheduledTable.getColumns().addAll(column1, column2, column3);
        TableView.TableViewSelectionModel<ScheduledTransaction> selectionModel = scheduledTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setScheduledTransactionsTableClickListeners();
    }

    /**
     * setting click listeners to transactions table
     */
    private void setTransactionClickListeners() {
        transactionsTable.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Transaction transaction = row.getItem();
                    displayTransaction(transaction);
                }
            });
            return row;
        });
        transactionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTransaction = newSelection;
                secondActionButton.setDisable(false);
            } else {
                selectedTransaction = null;
                secondActionButton.setDisable(true);
            }
        });
    }

    /**
     * setting click listeners to accounts table
     */
    private void setAccountsTableClickListeners() {
        accountsTable.setRowFactory(tv -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Account account = row.getItem();
                    displayAccount(account);
                }
            });
            return row;
        });
        accountsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (ledger.getAccounts().size() > 1) {
                if (newSelection != null) {
                    selectedAccount = newSelection;
                    secondActionButton.setDisable(false);
                } else {
                    selectedAccount = null;
                    secondActionButton.setDisable(true);
                }
            }
        });
    }

    private void setScheduledTransactionsTableClickListeners() {
        scheduledTable.setRowFactory(tv -> {
            TableRow<ScheduledTransaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ScheduledTransaction scheduledTransaction = row.getItem();
                    displayTransaction((Transaction) scheduledTransaction.getTransaction());
                }
            });
            return row;
        });

    }

    private void displayAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../AccountDialog.fxml"));
            Parent root = loader.load();
            AccountDialog controller = loader.getController();
            controller.setLedger(ledger);
            controller.setAccount(account);
            showDialog(root, "Account");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTransaction(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controller = loader.getController();
            controller.setLedger(ledger);
            controller.setTransaction(transaction);
            showDialog(root, "Transaction");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controller = loader.getController();
            controller.setLedger(ledger);
            showDialog(root, "Transaction");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../AccountDialog.fxml"));
            Parent root = loader.load();
            AccountDialog controller = loader.getController();
            controller.setLedger(ledger);
            showDialog(root, "Account");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewScheduledTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controller = loader.getController();
            controller.setScheduled(true);
            controller.setLedger(ledger);
            showDialog(root, "Transaction");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * show any dialog
     * @param root GUI of dialog
     * @param title title of dialog
     */
    private void showDialog(Parent root, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        refreshTables();
    }

    private void refreshTables() {
        refreshAccountsTable();
        refreshTransactionsTable();
        refreshScheduledTransactionsTable();
    }

    /**
     * setting ledger and displaying all of its information using tables
     *
     * @param ledger
     */
    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
        if(ListUtils.executeScheduledTransactions(ledger))
            saveChanges();
        refreshTables();
    }


    /**
     * save any changes made to ledger
     */
    private void saveChanges() {
        FileManager.saveToFile(saveFile.getPath(), this, GLedger.toJson(ledger));
    }

    private void refreshTransactionsTable() {
        transactions = (ArrayList<Transaction>) ListUtils.transformITransactions(ledger.getTransactions());
        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(transactions);
    }

    private void refreshAccountsTable() {
        accounts = (ArrayList<Account>) ListUtils.transformIAccount(ledger.getAccounts());
        accountsTable.getItems().clear();
        accountsTable.getItems().addAll(accounts);
    }


    private void refreshScheduledTransactionsTable() {
        scheduledTransactions = (ArrayList<ScheduledTransaction>) ListUtils.transformIScheduled(ledger.getScheduledTransaction());
        scheduledTable.getItems().clear();
        scheduledTable.getItems().addAll(scheduledTransactions);
    }
}
