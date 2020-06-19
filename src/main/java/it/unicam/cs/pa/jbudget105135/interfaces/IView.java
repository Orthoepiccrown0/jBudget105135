package it.unicam.cs.pa.jbudget105135.interfaces;

import java.io.IOException;

public interface IView {
    /**
     * Open view
     *
     * @param ledgerManager controller
     * @throws IOException if input throws an exception
     */
    void open(IController ledgerManager) throws IOException;

    /**
     * Close view
     */
    void close();
}
