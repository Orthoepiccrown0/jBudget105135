package it.unicam.cs.pa.jbudget105135.fxcontrollers;

import it.unicam.cs.pa.jbudget105135.Controller;
import it.unicam.cs.pa.jbudget105135.MovementType;
import it.unicam.cs.pa.jbudget105135.classes.Movement;
import it.unicam.cs.pa.jbudget105135.interfaces.IAccount;
import it.unicam.cs.pa.jbudget105135.interfaces.IMovement;
import it.unicam.cs.pa.jbudget105135.interfaces.ITableView;
import it.unicam.cs.pa.jbudget105135.utils.ListUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchByTagsView implements Initializable, ITableView {
    public TextField searchField;
    public TableView<Movement> table;
    private Controller controller;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }

    public void search() {
        if (searchField.getText().trim().length() > 0) {
            List<IMovement> tmpMovements = new ArrayList<>();
            for (IAccount account : controller.getLedger().getAccounts())
                tmpMovements.addAll(account.getMovements());

            List<IMovement> movementsContainingTags = ListUtils.searchMovementsByTag(tmpMovements, searchField.getText().trim());
            table.getItems().clear();
            table.getItems().addAll(ListUtils.transformIMovements(movementsContainingTags));
        }
    }

    @Override
    public void setupTable() {
        TableColumn<Movement, String> column1 = new TableColumn<>("Description");
        column1.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Movement, Double> column2 = new TableColumn<>("Amount");
        column2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Movement, MovementType> column3 = new TableColumn<>("Type");
        column3.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Movement, String> column4 = new TableColumn<>("Tags");
        column4.setCellValueFactory(new PropertyValueFactory<>("tagsString"));
        table.getColumns().addAll(column1, column2, column3, column4);
        TableView.TableViewSelectionModel<Movement> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
