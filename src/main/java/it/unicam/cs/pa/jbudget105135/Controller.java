package it.unicam.cs.pa.jbudget105135;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.interfaces.ILedger;
import it.unicam.cs.pa.jbudget105135.utils.FileManager;

import java.io.File;

public class Controller {
    private Gson GLedger;
    private ILedger ledger;
    private File saveFile;

    public Controller(ILedger ledger, File saveFile) {
        this.GLedger = new GsonBuilder().setPrettyPrinting().create();
        this.ledger = ledger;
        this.saveFile = saveFile;
    }

    public void saveChanges(){
        FileManager.saveToFile(saveFile.getPath(), GLedger.toJson(ledger));
    }

    public Gson getGLedger() {
        return GLedger;
    }

    public ILedger getLedger() {
        return ledger;
    }

    public File getSaveFile() {
        return saveFile;
    }



}
