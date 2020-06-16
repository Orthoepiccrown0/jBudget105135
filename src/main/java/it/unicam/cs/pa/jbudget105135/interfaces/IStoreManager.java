package it.unicam.cs.pa.jbudget105135.interfaces;

import it.unicam.cs.pa.jbudget105135.model.Ledger;

import java.io.File;

public interface IStoreManager {
    void save(String data,  File file);

    ILedger load(File file);
}
