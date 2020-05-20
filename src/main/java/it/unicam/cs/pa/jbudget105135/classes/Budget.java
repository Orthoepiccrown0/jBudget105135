package it.unicam.cs.pa.jbudget105135.classes;

import it.unicam.cs.pa.jbudget105135.interfaces.IBudget;
import it.unicam.cs.pa.jbudget105135.interfaces.ITag;

import java.util.List;

public class Budget implements IBudget {

    List<ITag> tags;


    @Override
    public List<ITag> getTags() {
        return null;
    }

    @Override
    public void set(ITag tag, double expected) {

    }

    @Override
    public double get(ITag tag) {
        return 0;
    }
}
