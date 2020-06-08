package it.unicam.cs.pa.jbudget105135.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.pa.jbudget105135.classes.*;
import it.unicam.cs.pa.jbudget105135.fxcontrollers.Main;
import it.unicam.cs.pa.jbudget105135.interfaces.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    /**
     * load data from file
     * @param path path of file
     * @param main link to the main class where ledger has to be imported;
     *             no other solutions was found
     */
    public static void loadDataFromFile(String path, Main main) {
        Task<String> task = new Task<>() {
            @Override
            public String call() throws InterruptedException, IOException {
                return readFile(path, StandardCharsets.UTF_8);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                main.progressBar.setVisible(false);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(IMovement.class, InterfaceSerializer.interfaceSerializer(Movement.class))
                        .registerTypeAdapter(ITransaction.class, InterfaceSerializer.interfaceSerializer(Transaction.class))
                        .registerTypeAdapter(IAccount.class, InterfaceSerializer.interfaceSerializer(Account.class))
                        .registerTypeAdapter(ITag.class, InterfaceSerializer.interfaceSerializer(Tag.class))
                        .registerTypeAdapter(IScheduledTransaction.class, InterfaceSerializer.interfaceSerializer(ScheduledTransaction.class))
                        .create();
                Ledger ledger = gson.fromJson(getValue(), Ledger.class);
                main.setLedger(ledger);
            }

        };
        main.progressBar.setVisible(true);
        new Thread(task).start();

    }

    /**
     * save all data into file rewriting it
     * @param path path to file
     * @param main link to main class to visualise progress
     * @param data data to be saved
     */
    public static void saveToFile(String path, Main main, String data) {
        Task<Path> task = new Task<>() {
            @Override
            public Path call() throws InterruptedException, IOException {
                return writeFile(path, data);
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                main.progressBar.setVisible(false);
            }


        };
        main.progressBar.setVisible(true);
        new Thread(task).start();
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private static Path writeFile(String path, String data) throws IOException {
        return Files.write(Paths.get(path), data.getBytes());
    }

}
