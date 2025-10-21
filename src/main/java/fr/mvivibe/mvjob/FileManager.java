package fr.mvivibe.mvjob;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_PATH = "plugins/MvJob/jobs.json";

    // Charge les données depuis le fichier
    public static Data loadData() {
        File file = new File(FILE_PATH);

        try {
            // Crée le dossier parent si nécessaire
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            // Crée le fichier si nécessaire
            if (!file.exists()) {
                file.createNewFile();
                Data emptyData = new Data(); // instance vide
                saveData(emptyData);          // sauvegarde initiale
                return emptyData;
            }

            try (FileReader reader = new FileReader(file)) {
                Data data = gson.fromJson(reader, Data.class);
                return data != null ? data : new Data();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new Data();
        }
    }

    // Sauvegarde les données sur le disque
    public static void saveData(Data data) {
        File file = new File(FILE_PATH);

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            if (!file.exists()) file.createNewFile();

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(data, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
