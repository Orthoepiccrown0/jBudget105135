package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import com.google.gson.Gson;
import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.classes.Account;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.ITransaction;
import it.unicam.cs.pa.jbudget105135.utils.FileManager;
import it.unicam.cs.pa.jbudget105135.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Main implements Initializable {
    public Pane controlsPane;
    public Pane transactionsBPane;
    public Pane accountsBPane;
    public Pane loadBPane;
    public Pane newBPane;

    public Button backButton;
    public Button firstActionButton;
    public Button secondActionButton;
    public Button thirdActionButton;

    public Label page;
//    public TableView<Transaction> table;

    public AnchorPane transactionPane;
    public AnchorPane movementPane;
    public AnchorPane accountPane;

    public TableView<Transaction> transactionsTable;
    public TableView<Account> accountsTable;
    public TableView<ScheduledTransaction> scheduledTable;

    public ProgressBar progressBar;

    private Gson GLedger;
    private ILedger ledger;
    private File saveFile;

    ArrayList<Transaction> transactions;
    ArrayList<Account> accounts;

    private enum PageType {
        TRANSACTIONS,
        MOVEMENTS,
        ACCOUNTS
    }

    private PageType currentPage;
    private PageType prevPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlsPane.setVisible(false);
        backButton.setDisable(true);
    }

    public void switchToTransactions(ContextMenuEvent contextMenuEvent) {
        currentPage = PageType.TRANSACTIONS;
        page.setText("Transactions");
        transactionsTable.setVisible(true);
        setupActionButtonsForTransactions();
        setupTableForTransactions();
    }


    public void switchToAccounts(ContextMenuEvent contextMenuEvent) {
        currentPage = PageType.ACCOUNTS;
    }

    public void createNewFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save jBudget file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        saveFile = fileChooser.showSaveDialog(backButton.getScene().getWindow());
        if (saveFile != null) {
            FileManager.saveToFile(saveFile.getPath(), this, "");
        }
        ledger = new Ledger();
        ledger.addAccount(AccountType.ASSETS, "name", "desc", 1500);
        switchToTransactions(null);
    }

    public void loadFromFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open jBudget .json file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());
        if (file != null) {
            FileManager.loadDataFromFile(file.getPath(), this);
        }

        switchToTransactions(null);
    }

    public void back(ActionEvent actionEvent) {
    }

    public void firstAction(ActionEvent actionEvent) {
        if(currentPage == PageType.TRANSACTIONS){
            addNewTransaction();
        }
    }



    public void secondAction(ActionEvent actionEvent) {
    }

    public void thirdAction(ActionEvent actionEvent) {
    }

    private void setupActionButtonsForTransactions() {
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

    private void setupActionButtonsForMovements() {
    }

    private void setupActionButtonsForAccounts() {
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
    }

    private void setupTableForAccounts() {
    }

    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
        setTableDataForTransactions();
    }

    private void setTableDataForTransactions() {
        transactionsTable.getItems().clear();
        transactionsTable.getItems().addAll(transactions);
    }

    private void setTableDataForAccounts() {
    }

    public void setGLedger(Gson GLedger) {
        this.GLedger = GLedger;
    }

    private void addNewTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newTransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controller = loader.getController();
            controller.setLedger(ledger);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Transaction");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            refreshTransactionsTable();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTransactionsTable() {
        transactions = (ArrayList<Transaction>) Utils.transformITransactions(ledger.getTransactions());
        setTableDataForTransactions();
    }

    private void addNewMovement() {
    }
}
