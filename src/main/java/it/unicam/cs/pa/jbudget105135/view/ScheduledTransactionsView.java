package it.unicam.cs.pa.jbudget105135.view;

import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.interfaces.ITableView;
import it.unicam.cs.pa.jbudget105135.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.model.Transaction;
import javafx.event.ActionEvent;
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

public class ScheduledTransactionsView implements Initializable, ITableView {
    public TableView<ScheduledTransaction> table;
    public DatePicker dateField;
    public Button deleteButton;
    private Controller controller;
    private List<ScheduledTransaction> scheduledTransactions;

    /**
     * Search button action
     */
    public void search(ActionEvent actionEvent) {
        if (dateField.getValue() == null)
            return;

        List<ScheduledTransaction> transactions =
                controller.searchScheduledTransactionByDate(scheduledTransactions, dateField.getValue());
        if (transactions == null)
            return;
        table.getItems().clear();
        table.getItems().addAll(transactions);
    }

    public void clearQuery(ActionEvent actionEvent) {
        dateField.setValue(null);
        table.getItems().clear();
        table.getItems().addAll(scheduledTransactions);
    }

    @Override
    public void setupTable() {
        TableColumn<ScheduledTransaction, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<ScheduledTransaction, Boolean> column2 = new TableColumn<>("Completed");
        column2.setCellValueFactory(new PropertyValueFactory<>("completed"));
        TableColumn<ScheduledTransaction, LocalDate> column3 = new TableColumn<>("Date");
        column3.setCellValueFactory(new PropertyValueFactory<>("date"));
        column1.setMinWidth(100);
        table.getColumns().add(column1);
        table.getColumns().add(column2);
        table.getColumns().add(column3);
        TableView.TableViewSelectionModel<ScheduledTransaction> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setScheduledTransactionsTableClickListeners();
    }

    private void setScheduledTransactionsTableClickListeners() {
        table.setRowFactory(tv -> {
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
        scheduledTransactions = controller.transformIScheduled(controller.getLedger().getScheduledTransaction());
        table.getItems().clear();
        table.getItems().addAll(scheduledTransactions);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        deleteButton.setDisable(true);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.scheduledTransactions = controller.transformIScheduled(controller.getLedger().getScheduledTransaction());
        this.table.getItems().clear();
        this.table.getItems().addAll(scheduledTransactions);
    }

    public void addScheduledTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../dialog/TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controllerFX = loader.getController();
            controllerFX.setScheduled(true);
            controllerFX.setController(controller);
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteScheduledTransaction() {
    }
}
