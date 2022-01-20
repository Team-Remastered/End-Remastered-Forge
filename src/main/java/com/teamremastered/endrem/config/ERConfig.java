package com.teamremastered.endrem.config;

import com.teamremastered.endrem.EndRemastered;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;


public class ERConfig {
    private static final Path FILE_PATH = FMLPaths.CONFIGDIR.get().resolve(EndRemastered.CONFIG_FILE);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static ConfigData configData = new ConfigData();

    public static void load() {
        if(FILE_PATH.toFile().exists()) {
            try {
                Reader reader = Files.newBufferedReader(FILE_PATH);
                configData = gson.fromJson(reader, ConfigData.class);
                if (configData == null) {
                    EndRemastered.LOGGER.error("Endrem: Invalid Config, will reset to default");
                    configData = new ConfigData();
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        save();
    }

    private static void save() {
        try {
            Writer writer = Files.newBufferedWriter(FILE_PATH);
            gson.toJson(configData, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigData getData(){
        return configData;
    }
}