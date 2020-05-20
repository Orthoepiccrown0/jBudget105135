package it.unicam.cs.pa.jbudget105135.interfaces;

public interface IBudgetManager {
    IBudgetReport generateReport(ILedger ledger, IBudget budget);
}
