package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IBudget;
import it.unicam.cs.pa.jbudget105135.interfaces.IBudgetManager;
import it.unicam.cs.pa.jbudget105135.interfaces.IBudgetReport;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;

public class BudgetManager implements IBudgetManager {
    //reserved for advanced ledger manager
    @Override
    public IBudgetReport generateReport(ILedger ledger, IBudget budget) {
        return null;
    }
}
