package it.unicam.cs.pa.jbudget105135.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.interfaces.*;
import it.unicam.cs.pa.jbudget105135.utils.InterfaceSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager implements IStoreManager{

    private final Gson gson;

    public FileManager() {
        gson = new GsonBuilder()
                .registerTypeAdapter(IMovement.class, InterfaceSerializer.interfaceSerializer(Movement.class))
                .registerTypeAdapter(ITransaction.class, InterfaceSerializer.interfaceSerializer(Transaction.class))
                .registerTypeAdapter(IAccount.class, InterfaceSerializer.interfaceSerializer(Account.class))
                .registerTypeAdapter(ITag.class, InterfaceSerializer.interfaceSerializer(Tag.class))
                .registerTypeAdapter(IScheduledTransaction.class, InterfaceSerializer.interfaceSerializer(ScheduledTransaction.class))
                .create();
    }

    private String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void writeFile(String path, String data) throws IOException {
        Files.write(Paths.get(path), data.getBytes());
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
            String data = readFile(file.getPath());
            return gson.fromJson(data, Ledger.class);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
