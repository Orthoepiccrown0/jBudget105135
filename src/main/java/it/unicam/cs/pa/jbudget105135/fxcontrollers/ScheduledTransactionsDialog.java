package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
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
import java.util.Date;
import java.util.ResourceBundle;

public class ScheduledTransactionsDialog implements Initializable {
    public CheckBox completedCheckBox;
    public DatePicker dateField;
    public TextArea descriptionField;
    public TableView<Transaction> transactionsTable;
    public Button submitButton;

    private ILedger ledger;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void submitAction(ActionEvent actionEvent) {
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

    }

    private void displayTransaction(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionDialog.fxml"));
            Parent root = loader.load();
            TransactionDialog controller = loader.getController();
            controller.setLedger(ledger);
            controller.setTransaction(transaction);
            showDialog(root, "Transaction");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
    }

    private void showDialog(Parent root, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
//        refreshTables();
    }
}
