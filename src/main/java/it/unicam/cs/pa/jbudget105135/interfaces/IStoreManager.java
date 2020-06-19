package it.unicam.cs.pa.jbudget105135.interfaces;

import java.io.File;

public interface IStoreManager {

    /**
     * Save data into file
     *
     * @param data data to save
     * @param file file to use
     */
    void save(String data, File file);

    /**
     * Loads data from file and transforms it into ILedger
     *
     * @param file file to use
     * @return ILedger object
     */
    ILedger load(File file);
}
