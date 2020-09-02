package net.cerulan.harvestcraftfabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigHandler {

    private final FoodsConfig foodsConfig;

    private ConfigHandler(FoodsConfig foodsConfig) {
        this.foodsConfig = foodsConfig;
    }

    public FoodsConfig getFoodsConfig() {
        return foodsConfig;
    }

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }

    public static ConfigHandler loadConfig() {
        Path harvestFolderPath = FabricLoader.getInstance().getConfigDir().resolve("harvestcraft");
        Path foodConfig = harvestFolderPath.resolve("foods.json");
        if (!tryDirectory(harvestFolderPath)) return null;
        if (!tryCreateFile(foodConfig)) return null;
        JsonObject foodObj;
        JsonObject config = tryReadConfig(foodConfig);
        if (config != null) foodObj = config;
        else foodObj = getFallbackConfig(foodConfig);

        FoodsConfig foodsConfigObj;
        try {
            foodsConfigObj = GSON.fromJson(foodObj, FoodsConfig.class);
        } catch (JsonSyntaxException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to deserialize config.");
            ex.printStackTrace();
            return null;
        }

        return new ConfigHandler(foodsConfigObj);
    }

    private static boolean tryDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
            return true;
        } catch (IOException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to create config directory");
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean tryCreateFile(Path file) {
        if (Files.notExists(file)) {
            byte[] content = getResource(file);
            try {
                Files.write(file, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException ex) {
                Harvestcraftfabric.LOGGER.error("Failed to write config file");
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static JsonObject tryReadConfig(Path file) {
        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(file);
        } catch (IOException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to read config file: " + file.getFileName().toString());
            ex.printStackTrace();
            return null;
        }
        JsonObject object = GSON.fromJson(reader, JsonObject.class);
        if (object == null) {
            Harvestcraftfabric.LOGGER.error("Failed to parse JSON config file: " + file.getFileName().toString());
        }
        return object;
    }

    private static byte[] getResource(Path file) {
        String justFile = file.getFileName().toString();
        InputStream stream = ConfigHandler.class.getClassLoader().getResourceAsStream(justFile);
        if (stream == null) {
            throw new RuntimeException("Failed to load file from jar: "+ justFile);
        }
        byte[] content;
        try {
            content = IOUtils.toByteArray(stream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file from jar: " + justFile, ex);
        }
        return content;
    }

    private static JsonObject getFallbackConfig(Path file) {
        byte[] content = getResource(file);
        InputStreamReader stream = new InputStreamReader(new ByteArrayInputStream(content));
        JsonObject object = GSON.fromJson(stream, JsonObject.class);
        if (object == null) {
            throw new RuntimeException("Fallback config file failed: " +  file.getFileName().toString());
        }
        return object;
    }

}
