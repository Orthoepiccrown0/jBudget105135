package it.unicam.cs.pa.jbudget105135;

import it.unicam.cs.pa.jbudget105135.classes.ConsoleIView;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.BasicController;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.IController;
import it.unicam.cs.pa.jbudget105135.interfaces.IView;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class App{


    private final IView IView;
    private final IController ledgerManager;

    public App(IView IView, IController ledgerManager) {
        this.IView = IView;
        this.ledgerManager = ledgerManager;
    }

    public static void main(String[] args) throws IOException {
        createBasicLedger().start();
    }

    public void start() throws IOException {
        IView.open(ledgerManager);
        IView.close();
    }

    private static App createBasicLedger() {
        HashMap<String, Consumer<? super ILedger>> commands = new HashMap<>();
        ILedger ledger = new Ledger();
        IView IView = new ConsoleIView<>(ledger,commands);
        IController ledgerManager = new BasicController<>(ledger, commands);
        return new App(IView, ledgerManager);
    }



}
