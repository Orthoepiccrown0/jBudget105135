package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import com.google.gson.Gson;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.utils.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Main implements Initializable {
    public Pane controlsPane;
    public Pane transactionsBPane;
    public Pane accountsBPane;
    public Pane loadBPane;
    public Pane newBPane;

    public Button backButton;
    public Button firstActionButton;
    public Button secondActionButton;
    public Button thirdActionButton;

    public Label page;
    public TableView table;

    public AnchorPane transactionPane;
    public AnchorPane movementPane;
    public AnchorPane accountPane;

    public ProgressBar progressBar;

    private Gson GLedger;
    private ILedger ledger;

    private enum PageType {
        TRANSACTIONS,
        MOVEMENTS,
        ACCOUNTS
    }

    private PageType currentPage;
    private PageType prevPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlsPane.setVisible(false);
        backButton.setDisable(true);
    }

    public void switchToTransactions(ContextMenuEvent contextMenuEvent) {
        currentPage = PageType.TRANSACTIONS;

    }

    public void switchToAccounts(ContextMenuEvent contextMenuEvent) {
        currentPage = PageType.ACCOUNTS;
    }

    public void createNewFile(MouseEvent mouseEvent) {
    }

    public void loadFromFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open jBudget .json file");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("JSON files", "*.json");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());
        if (file != null) {
            FileManager.loadDataFromFile(file.getPath(), this);
        }

    }

    public void back(ActionEvent actionEvent) {
    }

    public void firstAction(ActionEvent actionEvent) {
    }

    public void secondAction(ActionEvent actionEvent) {
    }

    public void thirdAction(ActionEvent actionEvent) {
    }
}
