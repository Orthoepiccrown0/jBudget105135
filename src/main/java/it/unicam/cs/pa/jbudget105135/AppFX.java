package it.unicam.cs.pa.jbudget105135;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App class used by JavaFX
 */
public class AppFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("JBudget");
        primaryStage.setScene(new Scene(root, 1000, 630));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(630);
        primaryStage.show();
    }

}
