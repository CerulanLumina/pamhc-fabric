package net.cerulan.harvestcraftfabric.pamassets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.pamassets.artifice.DataResource;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * This is downloaded on both physical server and client in order to have recipes and assets.
 * For client side assets, use ClientLocalPam
 */
public class LocalPam {

    protected static class Loader {
        private final Identifier identifier;
        private final Transformer<byte[]> processor;
        private final ZipArchiveEntry entry;

        public Loader(Identifier identifier, Transformer<byte[]> processor, ZipArchiveEntry entry) {
            this.identifier = identifier;
            this.processor = processor;
            this.entry = entry;
        }

        public Identifier getIdentifier() {
            return identifier;
        }

        public Transformer<byte[]> getTransformer() {
            return this.processor;
        }

        public ZipArchiveEntry getEntry() {
            return entry;
        }
    }

    protected static byte[] noopProcessor(byte[] bytes) {
        return bytes;
    }

    protected ZipFile zipFile;
    protected PamContent content;
    protected Gson gson;

    public LocalPam() {
        this(FabricLoader.getInstance().getGameDir().resolve("pamhc/harvestcraft.jar"));
    }

    private LocalPam(Path path) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            this.gson = new GsonBuilder().create();
        }
        verifyLocalFiles(path);
        try {
            this.zipFile = new ZipFile(path.toFile());
        } catch (IOException e) {
            Harvestcraftfabric.LOGGER.info("Failed to load local harvestcraft");
            throw new RuntimeException(e);
        }
        this.content = loadPamContent();
        Harvestcraftfabric.LOGGER.debug(content.getCrops());
        Harvestcraftfabric.LOGGER.debug(content.getFoods());
        Harvestcraftfabric.LOGGER.debug(content.getTools());
        Harvestcraftfabric.LOGGER.debug(content.getIngredients());
        Harvestcraftfabric.LOGGER.debug(content.getFruits());
    }

    protected LocalPam(LocalPam pam) {
        this.zipFile = pam.zipFile;
        this.content = pam.content;
        this.gson = pam.gson;
    }

    public PamContent getContent() {
        return this.content;
    }

    public void registerPamData(ArtificeResourcePack.ServerResourcePackBuilder builder) {
        Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            Optional<Loader> loader = getLoader(entry);
            loader.ifPresent(l -> registerDatum(l, builder));
        }
        if (!unhandledOres.isEmpty()) {
            Harvestcraftfabric.LOGGER.error("There were unhandled ore cases. This is a bug!");
            unhandledOres.forEach(Harvestcraftfabric.LOGGER::error);
        }
        NameConversion.tags.forEach(tag -> builder.addItemTag(new Identifier(tag), tagBuilder -> {}));
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            StringBuilder tags = new StringBuilder();
            NameConversion.tags.stream().sorted().forEach(t -> tags.append(t).append("\n"));
            StringBuilder missingIng = new StringBuilder();
            missingIngredients.stream().sorted().forEach(t -> missingIng.append(t).append("\n"));
            try {
                Files.write(FabricLoader.getInstance().getGameDir().resolve("../tags_out.txt"), tags.toString().getBytes(), StandardOpenOption.CREATE);
                Files.write(FabricLoader.getInstance().getGameDir().resolve("../missing_ingredients.txt"), missingIng.toString().getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerDatum(Loader loader, ArtificeResourcePack.ServerResourcePackBuilder builder) {
        try {
            InputStream is = zipFile.getInputStream(loader.getEntry());
            byte[] data = loader.getTransformer().transform(IOUtils.toByteArray(is));
            if (data == NO_RESULT_ITEM) {
                Harvestcraftfabric.LOGGER.debug("Not implemented skip: " + loader.getIdentifier());
            }
            else if (data != null) {
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    Path out = FabricLoader.getInstance()
                            .getGameDir()
                            .resolve("../recipe_out")
                            .resolve(loader.getIdentifier().getPath());
                    Files.createDirectories(out.getParent());
                    Files.write(out, data, StandardOpenOption.CREATE);
                }
                DataResource resource = new DataResource(data);
                Harvestcraftfabric.LOGGER.debug("Loading data " + loader.getIdentifier());
                builder.add(loader.getIdentifier(), resource);
            } else {
                Harvestcraftfabric.LOGGER.warn("Transformer skipped " + loader.getIdentifier());
            }
        } catch (IOException ex) {
            Harvestcraftfabric.LOGGER.error("Failed to load data " + loader.getIdentifier());
        }
    }

    private static final HashSet<String> skipRecipes = new HashSet<>();
    private static final HashSet<String> missingIngredients = new HashSet<>();

    private Optional<Loader> getLoader(ZipArchiveEntry entry) {
        String zipName = entry.getName();
        if (zipName.startsWith("assets/harvestcraft/recipes/") && zipName.endsWith(".json") && !skipRecipes.contains(zipName)) {
            if (zipName.contains("dustSalt")) return Optional.empty();
            Identifier id = getRecipeID(zipName);
            if (id != null) {
                return Optional.of(new Loader(id, this::transformRecipe, entry));
            } else {
                Harvestcraftfabric.LOGGER.error("Invalid recipe ID: " + zipName);
                return Optional.empty();
            }

        } else {
            return Optional.empty();
        }
    }

    private static Identifier getRecipeID(String zipName) {
        String path = zipName
                .substring(20)
                .toLowerCase();
        if (Identifier.isValid(path))
            return new Identifier("harvestcraft", path);
        else return null;
    }

    private static final byte[] NO_RESULT_ITEM = { 1, 2, 3, 4 };

    private byte[] transformRecipe(byte[] inputRecipe) {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(inputRecipe));
        JsonObject object = this.gson.fromJson(reader, JsonObject.class);

        JsonElement element = object.get("result");
        if (element.isJsonPrimitive()) {
            String result = object.getAsJsonPrimitive("result").getAsString();
            if (!Registry.ITEM.getOrEmpty(new Identifier(result)).isPresent()) {
                return NO_RESULT_ITEM;
            }
        } else if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            String result = obj.getAsJsonPrimitive("item").getAsString();
            if (!Registry.ITEM.getOrEmpty(new Identifier(result)).isPresent()) {
                return NO_RESULT_ITEM;
            }
        }

        String type = object.getAsJsonPrimitive("type").getAsString();
        switch (type) {
            case "forge:ore_shapeless":
            case "minecraft:crafting_shapeless":
                return shapelessTransform(object);
            case "forge:ore_shaped":
            case "minecraft:crafting_shaped":
                return shapedTransform(object);
            default:
                Harvestcraftfabric.LOGGER.error("Unknown recipe type");
                return null;
        }
    }

    private static final HashSet<String> unhandledOres = new HashSet<>();

    private byte[] shapelessTransform(JsonObject object) {
        for (JsonElement ingredient : object.getAsJsonArray("ingredients")) {
            if (modifyIngredient(ingredient.getAsJsonObject()) != IngredientModifyResult.Success) return null;
        }
        object.addProperty("type", "minecraft:crafting_shapeless");
        return gson.toJson(object).getBytes();
    }

    private byte[] shapedTransform(JsonObject object) {
        for (Map.Entry<String, JsonElement> ingredient : object.getAsJsonObject("key").entrySet()) {
            if (modifyIngredient(ingredient.getValue().getAsJsonObject()) != IngredientModifyResult.Success) return null;
        }
        object.addProperty("type", "minecraft:crafting_shaped");
        return gson.toJson(object).getBytes();
    }

    private IngredientModifyResult modifyIngredient(JsonObject ingred) {
        if (ingred.has("data")) {
            Harvestcraftfabric.LOGGER.error("Data field present");
            return IngredientModifyResult.DataFieldPresent;
        }
        if (ingred.has("type")) {
            String type = ingred.getAsJsonPrimitive("type").getAsString();
            if (type.equals("forge:ore_dict")) {
                String ore = ingred.getAsJsonPrimitive("ore").getAsString();
                NameConversion conversion = NameConversion.fromOre(ore);
                if (conversion == null) {
                    unhandledOres.add(ore);
                    Harvestcraftfabric.LOGGER.error("Unhandled ore case");
                    return IngredientModifyResult.UnhandledOre;
                }
                ingred.remove("ore");
                ingred.remove("type");
                if (conversion.isTag()) {
                    ingred.addProperty("tag", conversion.getValue());
                } else {
                    ingred.addProperty("item", conversion.getValue());
                }
            } else {
                Harvestcraftfabric.LOGGER.error("Unknown ingredient type.");
                return IngredientModifyResult.UnknownType;
            }
        }
        if (ingred.has("item")) {
            Identifier initialItem = new Identifier(ingred.getAsJsonPrimitive("item").getAsString());
            NameConversion conversion = NameConversion.fromItem(initialItem);
            if (conversion != null) {
                if (conversion.isTag()) {
                    ingred.remove("item");
                    ingred.addProperty("tag", conversion.getValue());
                } else {
                    ingred.addProperty("item", conversion.getValue());
                }
            }
        }
        if (ingred.has("item")) {
            Identifier initialItem = new Identifier(ingred.getAsJsonPrimitive("item").getAsString());
            if (!Registry.ITEM.getOrEmpty(initialItem).isPresent()) {
                Harvestcraftfabric.LOGGER.error("Missing ingredient: " + initialItem.toString());
                missingIngredients.add(initialItem.toString());
            }
        }
        return IngredientModifyResult.Success;
    }

    private static PamContent loadPamContent() {
        InputStream stream = LocalPam.class.getClassLoader().getResourceAsStream("pamhc.json");
        if (stream == null) {
            Harvestcraftfabric.LOGGER.error("Failed to load Pam Content file. This is a bug!");
            throw new RuntimeException("Failed to load Pam Content file.");
        }
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(new InputStreamReader(stream), PamContent.class);
    }

    private static void verifyLocalFiles(Path jar) {
        Harvestcraftfabric.LOGGER.info("Checking local harvestcraft");
        Path jarParent = jar.getParent();
        if (!Files.isDirectory(jarParent)) {
            if (!jarParent.toFile().mkdirs()) {
                Harvestcraftfabric.LOGGER.error("Failed to create directory `pamhc`. Is there a file conflict?");
                throw new RuntimeException();
            }
        }
        if (Files.notExists(jar)) {
            downloadHarvestcraft(jar);
        } else if (Files.isDirectory(jar)) {
            Harvestcraftfabric.LOGGER.error("Failed to validate existing jar file `pamhc/harvestcraft.jar`. Is there a file conflict?");
            throw new RuntimeException();
        }
    }

    private static void downloadHarvestcraft(Path output) {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("harvestcraftfabric");
        if (!container.isPresent()) {
            throw new RuntimeException("Failed to get mod container");
        }
        String strUrl = container.get().getMetadata().getCustomValue("harvestcraftfabric:pamurl").getAsString();
        try {
            URL url = new URL(strUrl);
            Harvestcraftfabric.LOGGER.info("Downloading harvestcraft from " + strUrl);
            FileUtils.copyURLToFile(url, output.toFile());
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Failed to parse URL", ex);
        } catch (IOException e) {
            Harvestcraftfabric.LOGGER.error("Failed to download Pam's Harvestcraft.\n" +
                    "If you are offline, download a copy of Pam's Harvestcraft for 1.12.2\n" +
                    "and place it in the folder 'pamhc' in the Minecraft root directory, naming it 'harvestcraft.jar'.");
            throw new RuntimeException("Failed to download harvestcraft", e);
        }
    }

    static {
        skipRecipes.add("assets/harvestcraft/recipes/peanutbuttercookiesitem - Copy (2).json");
        skipRecipes.add("assets/harvestcraft/recipes/pizzasliceitem_anchovypepperonipizzaitem_x13 - Copy (2).json");
        skipRecipes.add("assets/harvestcraft/recipes/plainyogurtitem_x4 - Copy.json");
        skipRecipes.add("assets/harvestcraft/recipes/cake.json");
        skipRecipes.add("assets/harvestcraft/recipes/honey.json");
        skipRecipes.add("assets/harvestcraft/recipes/honeyitem_x9_honey.json");
    }

    private enum IngredientModifyResult {
        Success,
        DataFieldPresent,
        UnhandledOre,
        UnknownType
    }

}
