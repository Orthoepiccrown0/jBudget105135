package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.List;
import java.util.Map;

public interface IBudgetReport {
    List<ITag> getTags();

    Map<ITag, Double> getReport();

    IBudget getBudget();

    double get();
}
