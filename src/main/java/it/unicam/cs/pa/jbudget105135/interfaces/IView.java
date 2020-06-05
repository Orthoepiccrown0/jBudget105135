package it.unicam.cs.pa.jbudget105135.interfaces;

import java.io.IOException;

public interface IView {
    void open(IController ledgerManager) throws IOException;

    void close();
}
