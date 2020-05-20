package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IBudget;
import it.unicam.cs.pa.jbudget105135.interfaces.IBudgetReport;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.List;
import java.util.Map;

public class BudgetReport implements IBudgetReport {

    private List<ITag> tags;
    private Map<ITag, Double> report;
    private IBudget budget;

    @Override
    public List<ITag> getTags() {
        return tags;
    }

    @Override
    public Map<ITag, Double> getReport() {
        return report;
    }

    @Override
    public IBudget getBudget() {
        return budget;
    }

    @Override
    public double get() {
        return 0;
    }
}
