package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.classes.Tag;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class TransactionDialog implements Initializable {
    public TextField nameField;
    public TextArea tagsField;
    public TableView<Movement> movementsTable;
    public Button addMovementButton;
    public Button deleteMovementButton;
    public Button addTransactionButton;
    public Button cancelButton;
    public Label error;
    public DatePicker datePicker;
    private ILedger ledger;

    //    public List<Movement> movements = new ArrayList<>();
    public List<Movement> movements = new ArrayList<>();
    private List<IMovement> imovements = new ArrayList<>();

    //transaction to show
    private Transaction transaction;


    public void addMovement(ActionEvent actionEvent) {
        if (datePicker.getValue() == null) {
            setErrorMessage("Error: Please pick the transaction date");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../newMovementDialog.fxml"));
            Parent root = loader.load();
            MovementDialog controller = loader.getController();
            controller.setMovements(movements);
            controller.setAccounts((ArrayList<IAccount>) ledger.getAccounts());
            controller.setDate(extractDateFromPicker());
            controller.setMovementTableView(movementsTable);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Movement");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovement(ActionEvent actionEvent) {

    }

    public void addTransaction(ActionEvent actionEvent) {
        if (isValidTransaction()) {
            imovements.addAll(movements);
            Transaction transaction = new Transaction(UUID.randomUUID().toString(), imovements, generateTags(),
                    extractDateFromPicker(), nameField.getText());
            ledger.addTransaction(transaction);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }

    private List<ITag> generateTags() {
        List<ITag> tags = new ArrayList<>();
        String[] data = tagsField.getText().split(",");
        for (String name : data) {
            tags.add(new Tag(name));
        }

        return tags;
    }

    private boolean isValidTransaction() {
        if (movements.size() == 0) {
            setErrorMessage("Error: You should add at least one movement");
            return false;
        }
        if (nameField.getText().length() == 0) {
            setErrorMessage("Error: You should insert name of transaction");
            return false;
        }
        if (tagsField.getText().length() == 0) {
            setErrorMessage("Error: Please insert at least one tag");
            return false;
        }
        if (datePicker.getValue() == null) {
            setErrorMessage("Error: Please pick the transaction date");
            return false;
        }
        return true;
    }

    private Date extractDateFromPicker() {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void setErrorMessage(String msg) {
        error.setVisible(true);
        error.setText(msg);
    }

    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        setActionButtonsForView();
    }

    private void setActionButtonsForView() {
        addTransactionButton.setText("OK");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMovementsTable();
    }

    private void setupMovementsTable() {
        TableColumn<Movement, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movement, Double> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Movement, Double> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));

        movementsTable.getColumns().addAll(column1, column2, column3);
        TableView.TableViewSelectionModel<Movement> selectionModel = movementsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }
}
