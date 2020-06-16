package it.unicam.cs.pa.jbudget105135.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.control.Controller;
import it.unicam.cs.pa.jbudget105135.interfaces.*;
import it.unicam.cs.pa.jbudget105135.model.*;
import it.unicam.cs.pa.jbudget105135.utils.InterfaceSerializer;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager implements IStoreManager{

    private Gson gson;

    public FileManager() {
        gson = new GsonBuilder()
                .registerTypeAdapter(IMovement.class, InterfaceSerializer.interfaceSerializer(Movement.class))
                .registerTypeAdapter(ITransaction.class, InterfaceSerializer.interfaceSerializer(Transaction.class))
                .registerTypeAdapter(IAccount.class, InterfaceSerializer.interfaceSerializer(Account.class))
                .registerTypeAdapter(ITag.class, InterfaceSerializer.interfaceSerializer(Tag.class))
                .registerTypeAdapter(IScheduledTransaction.class, InterfaceSerializer.interfaceSerializer(ScheduledTransaction.class))
                .create();
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private Path writeFile(String path, String data) throws IOException {
        return Files.write(Paths.get(path), data.getBytes());
    }

    /**
     * save all data into file rewriting it
     * @param data data to save
     * @param file file to use
     */
    @Override
    public void save(String data, File file) {
        try {
            writeFile(file.getPath(), data);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * load data from file
     * @param file file to load from
     * @return ledger
     */
    @Override
    public ILedger load(File file) {
        try {
            String data = readFile(file.getPath(), StandardCharsets.UTF_8);
            return gson.fromJson(data, Ledger.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
