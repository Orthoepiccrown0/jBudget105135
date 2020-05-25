package it.unicam.cs.pa.jbudget105135;

import it.unicam.cs.pa.jbudget105135.classes.ConsoleIView;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.BasicLedgerManager;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedgerManager;
import it.unicam.cs.pa.jbudget105135.interfaces.IView;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class AppTest<T extends ILedger> {


    private final IView IView;
    private final ILedgerManager ledgerManager;

    public AppTest(IView IView, ILedgerManager ledgerManager) {
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

    private static AppTest createBasicLedger() {
        HashMap<String, Consumer<? super ILedger>> commands = new HashMap<>();
        ILedger ledger = new Ledger();
        IView IView = new ConsoleIView<>(ledger,commands);
        ILedgerManager ledgerManager = new BasicLedgerManager<>(ledger, commands);
        return new AppTest(IView, ledgerManager);
    }


}
