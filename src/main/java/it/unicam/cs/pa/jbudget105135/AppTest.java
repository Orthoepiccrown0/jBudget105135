package it.unicam.cs.pa.jbudget105135;

import it.unicam.cs.pa.jbudget105135.classes.ConsoleView;
import it.unicam.cs.pa.jbudget105135.classes.Ledger;
import it.unicam.cs.pa.jbudget105135.classes.ConsoleLedgerManager;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedgerManager;
import it.unicam.cs.pa.jbudget105135.interfaces.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class AppTest<T extends ILedger> {


    private final View view;
    private final ILedgerManager ledgerManager;

    public AppTest(View view, ILedgerManager ledgerManager) {
        this.view = view;
        this.ledgerManager = ledgerManager;
    }

    public static void main(String[] args) throws IOException {
        createBasicLedger().start();
    }

    public void start() throws IOException {
        view.open(ledgerManager);
        view.close();
    }

    private static AppTest createBasicLedger() {
        HashMap<String, Consumer<? super ILedger>> commands = new HashMap<>();
        ILedger ledger = new Ledger();
        View view = new ConsoleView<>(ledger, commands);
        ILedgerManager ledgerManager = new ConsoleLedgerManager<>(ledger, commands);
        return new AppTest(view, ledgerManager);
    }


}
