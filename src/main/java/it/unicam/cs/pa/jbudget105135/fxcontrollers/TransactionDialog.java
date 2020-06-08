package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.classes.ScheduledTransaction;
import it.unicam.cs.pa.jbudget105135.classes.Transaction;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import it.unicam.cs.pa.jbudget105135.utils.ListUtils;
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

    private ILedger ledger;

    public List<Movement> movements = new ArrayList<>();
    private List<IMovement> imovements = new ArrayList<>();
    private List<ITag> tags = new ArrayList<>();
    private boolean scheduled;

    //transaction to show
    private Transaction transaction;


    public void addMovement(ActionEvent actionEvent) {
        if (datePicker.getValue() == null) {
            setErrorMessage("Error: Please pick the transaction date");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../MovementDialog.fxml"));
            Parent root = loader.load();
            MovementDialog controller = loader.getController();
            controller.setMovements(movements);
            controller.setAccounts((ArrayList<IAccount>) ledger.getAccounts());
            controller.setDate(extractDateFromPicker());
            controller.setTagsList(tags);
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

    public void deleteMovement(ActionEvent actionEvent) {

    }

    public void addTransaction(ActionEvent actionEvent) {
        if (isValidTransaction()) {
            if (transaction == null) {
                imovements.clear();
                imovements.addAll(movements);
                Transaction transaction = new Transaction(UUID.randomUUID().toString(), imovements, tags,
                        extractDateFromPicker(), nameField.getText());
                if (scheduled) {
                    String description = requestDescription();
                    if(description.equals(""))
                        displayQuitMessage();
                    ScheduledTransaction scheduledTransaction = new ScheduledTransaction(description,transaction);
                    ledger.addScheduledTransaction(scheduledTransaction);
                } else {
                    ledger.addTransaction(transaction);
                }
                ledger.addTags(tags);
                close();
            } else {
                transaction.setDate(extractDateFromPicker());
                transaction.setName(nameField.getText());
                transaction.setTags(tags);
                imovements.clear();
                imovements.addAll(movements);
                transaction.setMovements(imovements);
                ListUtils.searchTransactionAndReplaceIt(transaction, ledger);
                close();
            }
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

    public void cancel(ActionEvent actionEvent) {
        close();
    }

    private void setErrorMessage(String msg) {
        error.setVisible(true);
        error.setText(msg);
    }

    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
        setDateRange();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        setActionButtonsForView();
        unpack();
    }

    private void unpack() {
        movements = ListUtils.transformIMovements(transaction.getMovements());
        imovements = transaction.getMovements();
        nameField.setText(transaction.getName());
        tagsField.setText(generateStringOfTags(transaction.getTags()));
        datePicker.setValue(transaction.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        movementsTable.getItems().clear();
        movementsTable.getItems().addAll(movements);
        addMovementButton.setDisable(true);
        datePicker.setDisable(true);
    }

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

    private void setupMovementsTable() {
        TableColumn<Movement, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movement, Double> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Movement, Double> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Movement, Date> column4 = new TableColumn<>("Tags");
        column4.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        movementsTable.getColumns().addAll(column1, column2, column3, column4);
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
                        System.out.println(now.toString());
                        System.out.println(newValue.toString());
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
