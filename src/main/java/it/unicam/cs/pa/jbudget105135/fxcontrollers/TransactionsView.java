package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.Controller;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ITableView;
import it.unicam.cs.pa.jbudget105135.utils.ListUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionsView implements Initializable, ITableView {
    public TableView<Transaction> table;
    public Button deleteButton;
    private List<Transaction> transactions;
    private Transaction selectedTransaction;

    private Controller controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        deleteButton.setDisable(true);
    }

    public void addTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog transactionDialog = loader.getController();
            transactionDialog.setLedger(controller.getLedger());
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction() {
        if (selectedTransaction != null) {
            ListUtils.searchTransactionDeleteIt(selectedTransaction, controller.getLedger());
            refreshTable();
            controller.saveChanges();
        }
    }

    @Override
    public void setupTable() {
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
        table.getColumns().addAll(column1, column2, column3, column4, column5);
        TableView.TableViewSelectionModel<Transaction> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setTransactionClickListeners();
    }

    /**
     * setting click listeners to transactions table
     */
    private void setTransactionClickListeners() {
        table.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Transaction transaction = row.getItem();
                    displayTransaction(transaction);
                }
            });
            return row;
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTransaction = newSelection;
                deleteButton.setDisable(false);
            } else {
                selectedTransaction = null;
                deleteButton.setDisable(true);
            }
        });
    }

    private void displayTransaction(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog transactionDialog = loader.getController();
            transactionDialog.setLedger(controller.getLedger());
            transactionDialog.setTransaction(transaction);
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Transaction");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        refreshTable();
    }

    private void refreshTable() {
        transactions = ListUtils.transformITransactions(controller.getLedger().getTransactions());
        table.getItems().clear();
        table.getItems().addAll(transactions);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        refreshTable();
    }
}
