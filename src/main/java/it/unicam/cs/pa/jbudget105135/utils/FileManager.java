package it.unicam.cs.pa.jbudget105135.utils;

import it.unicam.cs.pa.jbudget105135.fxcontrollers.Main;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    private String path;

    public FileManager(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static void loadDataFromFile(String path, Main main) {
        Task task = new Task<String>() {
            @Override
            public String call() throws InterruptedException, IOException {
                return readFile(path, StandardCharsets.UTF_8);
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

    public static void saveToFile(String path, Main main, String data){
        Task task = new Task<Path>() {
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
