package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.classes.Tag;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MovementDialog implements Initializable {
    public TextArea description;
    public TextField amount;

    public ComboBox<MovementType> movementType;
    public ComboBox<IAccount> account;

    public TextArea tags;

    public Label errorMessage;

    public Button okButton;
    public Button cancelButton;
    public DatePicker datePicker;

    private ArrayList<Movement> movements;
    private ArrayList<IAccount> accounts;
    private Date date;
    private TableView<Movement> movementTableView;

    //used to display details of already existing movement
    private Movement movement;

    public void setMovementTableView(TableView<Movement> movementTableView) {
        this.movementTableView = movementTableView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forceNumericInputOnAmount();
        fillMovementTypesBox();

    }

    private void fillMovementTypesBox() {
        movementType.getItems().addAll(MovementType.values());
    }

    private void fillAccountsBox() {
        account.getItems().addAll(accounts);
    }

    //force amount input to numbers-only
    private void forceNumericInputOnAmount() {
        amount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void referMovements(ArrayList<Movement> movements) {
        this.movements = movements;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
        unpack();
    }

    private void unpack() {
    }

    public void submitAction(ActionEvent actionEvent) {
        if (isValidMovement()) {
            Movement movement = new Movement(description.getText(), Double.parseDouble(amount.getText()),
                    movementType.getSelectionModel().getSelectedItem(),
                    parseTags(), null, account.getSelectionModel().getSelectedItem(), new Date());
            movements.add(movement);
            refreshTable();
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }

    private void refreshTable() {
        movementTableView.getItems().addAll(movements);
    }

    private List<ITag> parseTags() {
        List<ITag> tagsList = new ArrayList<>();
        String[] data = tags.getText().split(",");
        for (String name : data) {
            tagsList.add(new Tag(name));
        }
        return tagsList;
    }

    private boolean isValidMovement() {
        if (description.getText().length() == 0) {
            setErrorMessage("Error: no description inserted");
            return false;
        }
        if (movementType.getSelectionModel().isEmpty()) {
            setErrorMessage("Error: no movement type selected");
            return false;
        }

        if (account.getSelectionModel().isEmpty()) {
            setErrorMessage("Error: no account selected");
            return false;
        }

        if (tags.getText().length() == 0) {
            setErrorMessage("Error: insert some tags, please");
            return false;
        }
        if (!checkAmount()) {
            setErrorMessage("Error: Check your amount");
            return false;
        }

        if (!canAffordIt()) {
            setErrorMessage("Error: You can't afford it, choose another account");
            return false;
        }

        return true;
    }

    private boolean checkAmount() {
        try {
            if (amount.getText().length() != 0) {
                double am = Double.parseDouble(amount.getText());
                if (am <= 0)
                    return false;

            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean canAffordIt() {
        IAccount account = this.account.getSelectionModel().getSelectedItem();
        MovementType type = this.movementType.getSelectionModel().getSelectedItem();
        double amount = Double.parseDouble(this.amount.getText());
        if (type == MovementType.DEBIT) {
            if (account.getType() == AccountType.ASSETS) {
                if (account.getBalance() < amount) {
                    System.out.println("You cant afford this movement");
                    return false;
                } else {
                    account.decreaseBy(amount);
                }
            } else if (account.getType() == AccountType.LIABILITIES) {
                account.increaseBy(amount);
            }
        } else if (type == MovementType.CREDIT) {
            if (account.getType() == AccountType.ASSETS) {
                account.increaseBy(amount);
            } else if (account.getType() == AccountType.LIABILITIES) {
                if (account.getBalance() < amount) {
                    System.out.println("You cant afford this movement");
                    return false;
                } else {
                    account.decreaseBy(amount);
                }
            }
        }
        return true;
    }

    private void setErrorMessage(String s) {
        errorMessage.setVisible(true);
        errorMessage.setText(s);
    }

    public void setAccounts(ArrayList<IAccount> accounts) {
        this.accounts = accounts;
        fillAccountsBox();
    }

    public void setDate(Date date) {
        this.date = date;
        datePicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void cancelAction(ActionEvent actionEvent) {
    }
}
