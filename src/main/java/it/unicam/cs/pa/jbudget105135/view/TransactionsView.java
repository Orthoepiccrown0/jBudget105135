package it.unicam.cs.pa.jbudget105135.view;

import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.interfaces.ITableView;
import it.unicam.cs.pa.jbudget105135.model.Transaction;
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
import java.time.LocalDate;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../dialog/TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog transactionDialog = loader.getController();
            transactionDialog.setController(controller);
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTransaction() {
        if (selectedTransaction != null) {
            controller.searchTransactionDeleteIt(selectedTransaction);
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
        TableColumn<Transaction, LocalDate> column4 = new TableColumn<>("Date");
        column4.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        TableColumn<Transaction, String> column5 = new TableColumn<>("Tags");
        column5.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        table.getColumns().add(column1);
        table.getColumns().add(column2);
        table.getColumns().add(column3);
        table.getColumns().add(column4);
        table.getColumns().add(column5);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../dialog/TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog transactionDialog = loader.getController();
            transactionDialog.setController(controller);
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
        transactions = controller.transformITransactions(controller.getLedger().getTransactions());
        table.getItems().clear();
        table.getItems().addAll(transactions);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        refreshTable();
    }
}
