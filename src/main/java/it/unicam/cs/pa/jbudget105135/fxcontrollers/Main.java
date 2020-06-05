package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import com.google.gson.Gson;
import it.unicam.cs.pa.jbudget105135.classes.Account;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
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
import java.util.ResourceBundle;

public class Main implements Initializable {
    public Pane controlsPane;
    public Pane transactionsBPane;
    public Pane accountsBPane;
    public Pane tagsBPane;
    public Pane loadBPane;
    public Pane newBPane;
    public Pane scheduledTransactionsBPane;
    public Pane searchPane;

    public Button firstActionButton;
    public Button secondActionButton;
    public Button thirdActionButton;

    public Label page;

    public TableView<Transaction> transactionsTable;
    public TableView<Account> accountsTable;
    public TableView<ScheduledTransaction> scheduledTable;

    public ProgressBar progressBar;
    public AnchorPane noFileSelectedPane;
    public TextField searchField;

    private Gson GLedger = new Gson();
    private ILedger ledger;
    private File saveFile;

    ArrayList<Transaction> transactions;
    ArrayList<Account> accounts;

    private enum PageType {
        TRANSACTIONS,
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
        setupTableForAccounts();
        setupTableForTransactions();
    }

    public void searchByTag(ActionEvent actionEvent) {
    }

    public void switchToTransactions(MouseEvent contextMenuEvent) {
        currentPage = PageType.TRANSACTIONS;
        page.setVisible(true);
        page.setText("Transactions");
        accountsTable.setVisible(false);
        transactionsTable.setVisible(true);
        setupActionButtonsForTransactions();

    }


    public void switchToAccounts(MouseEvent contextMenuEvent) {
        currentPage = PageType.ACCOUNTS;
        page.setVisible(true);
        page.setText("Accounts");
        accountsTable.setVisible(true);
        transactionsTable.setVisible(false);
        setupActionButtonsForAccounts();

    }

    public void switchToTags(MouseEvent mouseEvent) {
    }

    public void switchToScheduledTransactions(MouseEvent mouseEvent) {
    }

    public void createNewFile(MouseEvent mouseEvent) {
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
            switchToAccounts(null);
            addNewAccount();
        }

    }



    public void loadFromFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open jBudget .json file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        saveFile = fileChooser.showOpenDialog(transactionsBPane.getScene().getWindow());
        if (saveFile != null) {
            FileManager.loadDataFromFile(saveFile.getPath(), this);
            noFileSelectedPane.setVisible(false);
            unlockMenu();
            switchToTransactions(null);
        }
    }

    private void unlockMenu() {
        transactionsBPane.setDisable(false);
        accountsBPane.setDisable(false);
//        scheduledTransactionsBPane.setDisable(false);
        tagsBPane.setDisable(false);
    }

    private void lockMenu(){
        transactionsBPane.setDisable(true);
        accountsBPane.setDisable(true);
        scheduledTransactionsBPane.setDisable(true);
        tagsBPane.setDisable(true);
    }

    public void firstAction(ActionEvent actionEvent) {
        if (currentPage == PageType.TRANSACTIONS) {
            addNewTransaction();
        }else if(currentPage == PageType.ACCOUNTS){
            addNewAccount();
        }
    }

    public void secondAction(ActionEvent actionEvent) {
        if (currentPage == PageType.TRANSACTIONS) {
            deleteSelectedItem();
        }else if(currentPage == PageType.ACCOUNTS){
            deleteSelectedItem();
        }
    }

    private void deleteSelectedItem() {
        if (currentPage == PageType.TRANSACTIONS) {
            if (selectedTransaction != null) {
                ListUtils.searchTransactionDeleteIt(selectedTransaction, ledger);
                refreshTransactionsTable();
            }
        }else if(currentPage == PageType.ACCOUNTS){

        }
        saveChanges();
    }

    public void thirdAction(ActionEvent actionEvent) {
        //future implementations
    }

    private void setupActionButtonsForTransactions() {
        setDeleteAndAddButtons();
    }

    private void setupActionButtonsForAccounts() {
        setDeleteAndAddButtons();
    }

    private void setDeleteAndAddButtons() {
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

    private void setupTableForAccounts() {
        TableColumn<Account, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Account, Integer> column2 = new TableColumn<>("Balance");
        column2.setCellValueFactory(new PropertyValueFactory<>("balance"));
        TableColumn<Account, Double> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Account, Date> column4 = new TableColumn<>("Description");
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
        transactionsTable.getColumns().addAll(column1, column2, column3, column4);
        TableView.TableViewSelectionModel<Transaction> selectionModel = transactionsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setTransactionClickListeners();
    }

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
            if (newSelection != null) {
                selectedAccount = newSelection;
                secondActionButton.setDisable(false);
            } else {
                selectedTransaction = null;
                secondActionButton.setDisable(true);
            }
        });
    }

    private void displayAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newAccountDialog.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newTransactionDialog.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newTransactionDialog.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newAccountDialog.fxml"));
            Parent root = loader.load();
            AccountDialog controller = loader.getController();
            controller.setLedger(ledger);
            showDialog(root, "Account");
            saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    }


    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
        refreshTransactionsTable();
        refreshAccountsTable();
    }



    private void setTableDataForTransactions() {
        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(transactions);
    }

    private void setTableDataForAccounts() {
        accountsTable.getItems().clear();
        accountsTable.getItems().addAll(accounts);
    }

    private void saveChanges() {
        FileManager.saveToFile(saveFile.getPath(), this, GLedger.toJson(ledger));
    }

    private void refreshTransactionsTable() {
        transactions = (ArrayList<Transaction>) ListUtils.transformITransactions(ledger.getTransactions());
        setTableDataForTransactions();
    }

    private void refreshAccountsTable() {
        accounts = (ArrayList<Account>) ListUtils.transformIAccount(ledger.getAccounts());
        setTableDataForAccounts();
    }
}
