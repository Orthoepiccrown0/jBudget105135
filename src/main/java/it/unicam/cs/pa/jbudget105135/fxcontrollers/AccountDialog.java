package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.classes.Account;
import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.utils.ListUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountDialog implements Initializable {
    public TextField nameField;
    public TextField openingBalanceField;

    public TextArea descriptionField;

    public ComboBox<AccountType> typeField;

    public TableView<Movement> movementsTable;

    public Button okButton;
    public Button cancelButton;

    public Label balanceLabel;
    public Label errorMessage;

    private ILedger ledger;
    private Account account;
    private ArrayList<Movement> movements = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMovementsTable();
        forceNumericInputOnBalance();
        fillAccountTypeBox();
    }

    private void fillAccountTypeBox() {
        typeField.getItems().addAll(AccountType.values());
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

    private void forceNumericInputOnBalance() {
        openingBalanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    openingBalanceField.setText(oldValue);
                }
            }
        });
    }

    public void setLedger(ILedger ledger) {
        this.ledger = ledger;
    }

    public void setAccount(Account account) {
        this.account = account;
        unpack();
    }

    private void unpack() {
        openingBalanceField.setDisable(true);
        typeField.setDisable(true);
        movements = (ArrayList<Movement>) ListUtils.transformIMovements(account.getMovements());
        setTableData();
        restoreFields();
    }

    private void restoreFields() {
        nameField.setText(account.getName());
        descriptionField.setText(account.getDescription());
        openingBalanceField.setText(String.valueOf(account.getOpeningBalance()));
        typeField.getSelectionModel().select(account.getType());
    }

    private void setTableData() {
        movementsTable.getItems().clear();
        movementsTable.getItems().addAll(movements);
    }

    public void submitAction(ActionEvent actionEvent) {
        if (isValidAccount()) {
            if (account == null) {
                createNewAccount();
                close();
            } else {
                account.setName(nameField.getText().trim());
                account.setDescription(descriptionField.getText().trim());
                ListUtils.searchAccountAndReplaceIt(account, ledger);
                close();
            }
        }
    }

    private void close() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    private void createNewAccount() {
        AccountType type = typeField.getSelectionModel().getSelectedItem();
        String name = nameField.getText().trim();
        String desc = descriptionField.getText().trim();
        double openingBalance = Double.parseDouble(openingBalanceField.getText());
        ledger.addAccount(type, name, desc, openingBalance);
    }

    private boolean isValidAccount() {
        if (nameField.getText().trim().length() == 0) {
            setErrorMessage("Error: please insert name");
            return false;
        }

        if (descriptionField.getText().trim().length() == 0) {
            setErrorMessage("Error: please insert description");
            return false;
        }

        if (openingBalanceField.getText().trim().length() == 0) {
            setErrorMessage("Error: please insert opening balance");
            return false;
        }

        if (typeField.getSelectionModel().isEmpty()) {
            setErrorMessage("Error: please choose account type");
            return false;
        }

        return true;
    }

    private void setErrorMessage(String msg) {
        errorMessage.setVisible(true);
        errorMessage.setText(msg);
    }

    public void cancelAction(ActionEvent actionEvent) {
        close();
    }
}