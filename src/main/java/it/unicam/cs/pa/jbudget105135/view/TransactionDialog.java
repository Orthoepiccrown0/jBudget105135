package it.unicam.cs.pa.jbudget105135.view;

import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.model.Movement;
import it.unicam.cs.pa.jbudget105135.model.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.model.Transaction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private Controller controller;

    public List<Movement> movements = new ArrayList<>();
    private List<IMovement> iMovements = new ArrayList<>();
    private final List<ITag> tags = new ArrayList<>();
    private boolean scheduled;

    //transaction to show
    private Transaction transaction;

    /**
     * Add movement button event. Shows new dialog to insert a new movement.
     */
    public void addMovement() {
        if (datePicker.getValue() == null) {
            setErrorMessage("Error: Please pick the transaction date");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MovementDialog.fxml"));
            Parent root = loader.load();

            MovementDialog movementDialog = loader.getController();
            movementDialog.setMovements(movements);
            movementDialog.setAccounts((ArrayList<IAccount>) controller.getLedger().getAccounts());
            movementDialog.setDate(extractDateFromPicker());
            movementDialog.setTagsList(tags);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Movement");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
            tagsField.setText(generateStringOfTags(tags));
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        movementsTable.getItems().clear();
        movementsTable.getItems().addAll(movements);
    }

    public void deleteMovement() {
        //future implementation
    }

    /**
     * Add new transaction or change an existing and close window.
     */
    public void addTransaction() {
        if (isValidTransaction()) {
            if (transaction == null) {
                iMovements.clear();
                iMovements.addAll(movements);
                Transaction transaction = new Transaction(UUID.randomUUID().toString(), iMovements, tags,
                        extractDateFromPicker(), nameField.getText());
                if (scheduled) {
                    String description = requestDescription();

                    if (description.equals(""))
                        displayQuitMessage();

                    ScheduledTransaction scheduledTransaction = new ScheduledTransaction(description, transaction);
                    controller.addScheduledTransaction(scheduledTransaction);
                } else {
                    controller.addTransaction(transaction);
                }
                controller.addTags(tags);
            } else {
                transaction.setDate(extractDateFromPicker());
                transaction.setName(nameField.getText());
                transaction.setTags(tags);
                iMovements.clear();
                iMovements.addAll(movements);
                transaction.setMovements(iMovements);
                controller.searchTransactionAndReplaceIt(transaction);
            }
            close();
        }
    }

    private void displayQuitMessage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Maybe we are misunderstood");
        alert.setContentText("Looks like u don't want to create scheduled transaction, bye! >:(");
        alert.showAndWait();

    }

    private String requestDescription() {

        TextInputDialog td = new TextInputDialog("Enter description for scheduled transaction");
        td.setHeaderText("Transaction description");
        td.showAndWait();
        return td.getEditor().getText();
    }

    private void close() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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

    public void cancel() {
        close();
    }

    private void setErrorMessage(String msg) {
        error.setVisible(true);
        error.setText(msg);
    }

    public void setController(Controller controller) {
        this.controller = controller;
        setDateRange();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        setActionButtonsForView();
        unpack();
    }

    private void unpack() {
        movements = controller.transformIMovements(transaction.getMovements());
        iMovements = transaction.getMovements();
        nameField.setText(transaction.getName());
        tagsField.setText(generateStringOfTags(transaction.getTags()));
        datePicker.setValue(transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        movementsTable.getItems().clear();
        movementsTable.getItems().addAll(movements);
        addMovementButton.setDisable(true);
        datePicker.setDisable(true);
    }

    /**
     * Generates string of tags used in all movements
     *
     * @param target list of tags
     * @return string of tags like (tag1, tag2, ...)
     */
    private String generateStringOfTags(List<ITag> target) {
        ArrayList<String> tags = new ArrayList<>();
        for (ITag tag : target) {
            tags.add(tag.toString());
        }
        return String.join(",", tags);
    }

    private void setActionButtonsForView() {
        addTransactionButton.setText("OK");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMovementsTable();
    }

    /**
     * Setup of movements table
     */
    private void setupMovementsTable() {
        TableColumn<Movement, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movement, Double> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Movement, MovementType> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Movement, Date> column4 = new TableColumn<>("Tags");
        column4.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        movementsTable.getColumns().add(column1);
        movementsTable.getColumns().add(column2);
        movementsTable.getColumns().add(column3);
        movementsTable.getColumns().add(column4);
        TableView.TableViewSelectionModel<Movement> selectionModel = movementsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
        setDateRange();
    }

    /**
     * set date range for scheduled transaction(only future dates are available)
     * and for normal transactions
     */
    private void setDateRange() {
        LocalDate now = LocalDate.now();
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (scheduled) {
                    if (newValue.isBefore(now) || newValue.isEqual(now)) {
                        datePicker.setValue(now.plusDays(1));
                    }
                } else {
                    if (newValue.isAfter(now)) {
                        datePicker.setValue(now);
                    }
                }
            }
        });

        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                    }
                });
    }
}
