package net.cerulan.harvestcraftfabric.pamassets;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.pamassets.artifice.DataResource;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ClientLocalPam extends LocalPam {
    public ClientLocalPam(LocalPam commonPam) {
        super(commonPam);
    }

    public void registerPamResources(ArtificeResourcePack.ClientResourcePackBuilder builder) {
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            Optional<Loader> loader = getLoader(entry);
            loader.ifPresent(l -> registerAsset(l, builder));
        }

    }

    private void registerAsset(Loader loader, ArtificeResourcePack.ClientResourcePackBuilder builder) {
        try {
            InputStream is = zipFile.getInputStream(loader.getEntry());
            DataResource resource = new DataResource(loader.getTransformer().transform(IOUtils.toByteArray(is)));
            Harvestcraftfabric.LOGGER.debug("Loading asset " + loader.getIdentifier());
            builder.add(loader.getIdentifier(), resource);
        } catch (IOException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to load asset " + loader.getIdentifier());
            ex.printStackTrace();
        }
    }

    private Optional<Loader> getLoader(ZipArchiveEntry entry) {
        String zipName = entry.getName();
        if (zipName.startsWith("assets/harvestcraft/textures/") && zipName.endsWith(".png"))
            return Optional.of(new Loader(getTextureID(zipName), LocalPam::noopProcessor, entry));
        else if (zipName.startsWith("assets/harvestcraft/models") && zipName.endsWith(".json"))
            return Optional.of(new Loader(getModelID(zipName), this::modelTransformer, entry));
        else if (zipName.startsWith("assets/harvestcraft/blockstates") && zipName.endsWith(".json"))
            return Optional.of(new Loader(getModelID(zipName), this::blockStateTransformer, entry));
        else if (zipName.startsWith("assets/harvestcraft/lang") && zipName.endsWith(".lang"))
            return Optional.of(new Loader(getLangFile(zipName), this::langFileTransformer, entry));
        else return Optional.empty();
    }

    private static Identifier getTextureID(String zipName) {
        String path = zipName
                .substring(20)
                .replace("textures/blocks", "textures/block")
                .replace("textures/items", "textures/item");
        return new Identifier("harvestcraft", path);
    }

    private static Identifier getModelID(String zipName) {
        String path = zipName
                .substring(20);
        return new Identifier("harvestcraft", path);
    }

    private static Identifier getLangFile(String zipName) {
        String path = zipName
                .substring(20)
                .replace(".lang", ".json");
        return new Identifier("harvestcraft", path);
    }

    private byte[] modelTransformer(byte[] model) {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(model));
        JsonObject object = this.gson.fromJson(reader, JsonObject.class);
        if (object.has("textures")) {
            for (Map.Entry<String, JsonElement> entry : object.get("textures").getAsJsonObject().entrySet()) {
                String string = entry.getValue().getAsString()
                        .replace("harvestcraft:blocks/", "harvestcraft:block/")
                        .replace("harvestcraft:items/", "harvestcraft:item/");
                entry.setValue(new JsonPrimitive(string));
            }
        }

        return this.gson.toJson(object).getBytes();
    }

    private byte[] blockStateTransformer(byte[] blockstate) {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(blockstate));
        JsonObject object = this.gson.fromJson(reader, JsonObject.class);
        if (object.has("variants")) {
            for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("variants").entrySet()) {
                if (entry.getValue().isJsonObject()) {
                    JsonObject obj = entry.getValue().getAsJsonObject();
                    String model = obj.getAsJsonPrimitive("model").getAsString();
                    obj.addProperty("model", model.replace("harvestcraft:", "harvestcraft:block/"));
                }
            }

        }
        String json = this.gson.toJson(object);
        return json.getBytes();
    }

    private byte[] langFileTransformer(byte[] lang) {
        try {
            List<String> lines = IOUtils.readLines(new ByteArrayInputStream(lang), StandardCharsets.UTF_8);
            HashMap<String, String> newLang = new HashMap<>();
            lines.stream().map(String::trim).filter(s -> s.length() > 1).map(s -> s.split("=")).filter(arr -> arr.length == 2).forEach(line -> {
                String key = line[0];
                String value = line[1];
                String newKey;
                if (key.startsWith("tile."))
                    newKey = "block.harvestcraft." + key.substring(5, key.length() - 5);
                else if (key.startsWith("harvestcraft.tile."))
                    newKey = "block.harvestcraft." + key.substring(18, key.length() - 5);
                else if (key.startsWith("item.minecraft:"))
                    return;
                else if (key.startsWith("item."))
                    if (key.endsWith(".name"))
                        newKey = "item.harvestcraft." + key.substring(5, key.length() - 5);
                    else
                        newKey = "item.harvestcraft." + key.substring(5);
                else newKey = key;
                newLang.put(newKey, value);

            });
            return this.gson.toJson(newLang).getBytes();
        } catch (IOException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to transform language file!");
            ex.printStackTrace();
            return "{}".getBytes();
        }
    }
}