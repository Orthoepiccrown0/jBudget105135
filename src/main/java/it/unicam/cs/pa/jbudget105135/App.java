package it.unicam.cs.pa.jbudget105135;

import it.unicam.cs.pa.jbudget105135.interfaces.IController;
import it.unicam.cs.pa.jbudget105135.interfaces.IView;
import javafx.application.Application;

import java.io.IOException;

/**
 * Main app class
 */
public class App {
    private final IView view;

    public App(IView view) {
        this.view = view;
    }

    public static void main(String[] args) {
        createGUI();
    }

    public void start(IController controller) throws IOException {
        view.open(controller);
        view.close();
    }

    public static void createGUI() {
        Application.launch(AppFX.class);
    }
}
