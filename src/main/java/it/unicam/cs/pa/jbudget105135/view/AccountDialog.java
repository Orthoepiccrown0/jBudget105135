package it.unicam.cs.pa.jbudget105135.view;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.model.Account;
import it.unicam.cs.pa.jbudget105135.model.Movement;
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

    private Controller controller;

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

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * display account
     *
     * @param account account to display
     */
    public void setAccount(Account account) {
        this.account = account;
        unpack();
    }

    private void unpack() {
        openingBalanceField.setDisable(true);
        typeField.setDisable(true);
        movements = (ArrayList<Movement>) controller.transformIMovements(account.getMovements());
        setTableData();
        restoreFields();
    }

    private void restoreFields() {
        nameField.setText(account.getName());
        descriptionField.setText(account.getDescription());
        openingBalanceField.setText(String.valueOf(account.getOpeningBalance()));
        typeField.getSelectionModel().select(account.getType());
        balanceLabel.setText("Balance: " + account.getBalance());
    }

    private void setTableData() {
        movementsTable.getItems().clear();
        movementsTable.getItems().addAll(movements);
    }

    public void submitAction(ActionEvent actionEvent) {
        if (isValidAccount()) {
            if (account == null) {
                createNewAccount();
            } else {
                account.setName(nameField.getText().trim());
                account.setDescription(descriptionField.getText().trim());
                controller.searchAccountAndReplaceIt(account);
            }
            close();
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
        IAccount account = new Account(type, name, desc, openingBalance);
        controller.addAccount(account);
    }

    /**
     * before submit check if it is a valid account
     *
     * @return true if so, false otherwise
     */
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
