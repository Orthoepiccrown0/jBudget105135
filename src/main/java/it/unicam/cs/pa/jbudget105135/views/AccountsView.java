package it.unicam.cs.pa.jbudget105135.views;

import it.unicam.cs.pa.jbudget105135.AccountType;
import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.fxcontrollers.AccountDialog;
import it.unicam.cs.pa.jbudget105135.interfaces.IController;
import it.unicam.cs.pa.jbudget105135.interfaces.ITableView;
import it.unicam.cs.pa.jbudget105135.model.Account;
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
import java.util.List;
import java.util.ResourceBundle;

public class AccountsView implements Initializable, ITableView {
    public Button deleteButton;
    public TableView<Account> table;

    private List<Account> accounts;
    private Account selectedAccount;
    private IController controller;

    public void addAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../dialog/AccountDialog.fxml"));
            Parent root = loader.load();
            AccountDialog accountDialog = loader.getController();
            accountDialog.setController(controller);
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount() {
        //todo: boh
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        deleteButton.setDisable(true);
    }

    @Override
    public void setupTable() {
        TableColumn<Account, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Account, Double> column2 = new TableColumn<>("Balance");
        column2.setCellValueFactory(new PropertyValueFactory<>("balance"));
        TableColumn<Account, AccountType> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Account, String> column4 = new TableColumn<>("Description");
        column4.setCellValueFactory(new PropertyValueFactory<>("description"));
        table.getColumns().addAll(column1, column2, column3, column4);
        TableView.TableViewSelectionModel<Account> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        setAccountsTableClickListeners();
    }

    /**
     * setting click listeners to accounts table
     */
    private void setAccountsTableClickListeners() {
        table.setRowFactory(tv -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Account account = row.getItem();
                    displayAccount(account);
                }
            });
            return row;
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (controller.getLedger().getAccounts().size() > 1) {
                if (newSelection != null) {
                    selectedAccount = newSelection;
                    deleteButton.setDisable(false);
                } else {
                    selectedAccount = null;
                    deleteButton.setDisable(true);
                }
            }
        });
    }

    private void displayAccount(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../dialog/AccountDialog.fxml"));
            Parent root = loader.load();
            AccountDialog controllerFX = loader.getController();
            controllerFX.setController(controller);
            controllerFX.setAccount(account);
            showDialog(root);
            controller.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(Parent root) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Account");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
        refreshTable();
    }

    private void refreshTable() {
        accounts = ListUtils.transformIAccount(controller.getLedger().getAccounts());
        table.getItems().clear();
        table.getItems().addAll(accounts);
    }

    public void setController(IController controller) {
        this.controller = controller;
        refreshTable();
    }
}
