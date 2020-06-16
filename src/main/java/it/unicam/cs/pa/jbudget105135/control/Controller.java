package it.unicam.cs.pa.jbudget105135.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.interfaces.*;
import it.unicam.cs.pa.jbudget105135.model.FileManager;

import java.io.File;
import java.util.List;

public class Controller implements IController {
    private final Gson GLedger;
    private final ILedger ledger;
    private final File saveFile;
    private boolean isOn;

    public Controller(ILedger ledger, File saveFile) {
        this.GLedger = new GsonBuilder().setPrettyPrinting().create();
        this.ledger = ledger;
        this.saveFile = saveFile;
        isOn = true;
    }

    @Override
    public void saveChanges() {
        FileManager fileManager = new FileManager();
        fileManager.save(GLedger.toJson(ledger), saveFile);
    }

    @Override
    public void addTransaction(ITransaction transaction) {
        ledger.addTransaction(transaction);
    }

    @Override
    public void addAccount(IAccount account) {
        ledger.addAccount(account);
    }

    @Override
    public void addScheduledTransaction(IScheduledTransaction transaction) {
        ledger.addScheduledTransaction(transaction);
    }

    @Override
    public void addTags(List<ITag> tags) {
        ledger.addTags(tags);
    }

    @Override
    public Gson getGLedger() {
        return GLedger;
    }

    @Override
    public ILedger getLedger() {
        return ledger;
    }

    @Override
    public File getSaveFile() {
        return saveFile;
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    @Override
    public void close() {
        isOn = false;
    }
}
