package it.unicam.cs.pa.jbudget105135.interfaces;

import java.util.List;

public interface IBudget {
    List<ITag> getTags();

    void set(ITag tag, double expected);

    double get(ITag tag);
}
