package it.unicam.cs.pa.jbudget105135.fxcontrollers;


import it.unicam.cs.pa.jbudget105135.Controller;
import it.unicam.cs.pa.jbudget105135.classes.*;
import it.unicam.cs.pa.jbudget105135.interfaces.IControllerCallback;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.utils.FileManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * The class  {@code Main} contains methods that are used to display data
 * and interact with it
 */
public class Main implements Initializable, IControllerCallback {
    public Pane controlsPane;
    public Pane transactionsBPane;
    public Pane accountsBPane;
    public Pane tagsBPane;
    public Pane loadBPane;
    public Pane newBPane;
    public Pane scheduledTransactionsBPane;

    public ProgressBar progressBar;
    public AnchorPane noFileSelectedPane;

    public AnchorPane rootPanel;

    private Controller controller;

    private enum PageType {
        TRANSACTIONS,
        TAGS,
        SCHEDULED_TRANSACTION,
        MOVEMENTS,  //for future implementation
        ACCOUNTS
    }

    private PageType currentPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lockMenu();
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        unlockMenu();
        try {
            switchToTransactions();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Switch current tab to transactions tab
     */
    public void switchToTransactions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../TransactionsView.fxml"));
        AnchorPane root = loader.load();
        TransactionsView controllerFX = loader.getController();
        controllerFX.setController(controller);
        rootPanel.getChildren().setAll(root);
    }

    /**
     * Switch current tab to accounts tab
     */
    public void switchToAccounts() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../AccountsView.fxml"));
        AnchorPane root = loader.load();
        AccountsView controllerFX = loader.getController();
        controllerFX.setController(controller);
        rootPanel.getChildren().setAll(root);
    }

    /**
     * Switch current tab to search by tags tab
     */
    public void switchToTags() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../SearchByTagsView.fxml"));
        AnchorPane root = loader.load();
        SearchByTagsView controllerFX = loader.getController();
        controllerFX.setController(controller);
        rootPanel.getChildren().setAll(root);
    }

    /**
     * Switch current tab to ScheduledTransactions tab
     */
    public void switchToScheduledTransactions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ScheduledTransactionsView.fxml"));
        AnchorPane root = loader.load();
        ScheduledTransactionsView controllerFX = loader.getController();
        controllerFX.setController(controller);
        rootPanel.getChildren().setAll(root);
    }

    /**
     * creating of new file in specific directory by using FileChooser
     */
    public void createNewFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save jBudget file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        File saveFile = fileChooser.showSaveDialog(transactionsBPane.getScene().getWindow());
        if (saveFile != null) {
            FileManager.saveToFile(saveFile.getPath(), "");
            Ledger ledger = new Ledger();
            noFileSelectedPane.setVisible(false);
            controller = new Controller(ledger, saveFile);
            unlockMenu();
            switchToAccounts();
        }

    }

    /**
     * loading from chosen file on mouse click
     */
    public void loadFromFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open jBudget .json file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        File saveFile = fileChooser.showOpenDialog(transactionsBPane.getScene().getWindow());
        if (saveFile != null) {
            FileManager.loadDataFromFile(saveFile, this);
            noFileSelectedPane.setVisible(false);
        }
    }

    private void unlockMenu() {
        transactionsBPane.setDisable(false);
        accountsBPane.setDisable(false);
        scheduledTransactionsBPane.setDisable(false);
        tagsBPane.setDisable(false);
    }

    private void lockMenu() {
        transactionsBPane.setDisable(true);
        accountsBPane.setDisable(true);
        scheduledTransactionsBPane.setDisable(true);
        tagsBPane.setDisable(true);
    }
}
