package net.cerulan.harvestcraftfabric.pamassets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.ArtificeResourcePack;
import com.swordglowsblue.artifice.api.builder.data.LootTableBuilder;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.block.PamCropBlock;
import net.cerulan.harvestcraftfabric.mixin.AccessorIdentifier;
import net.cerulan.harvestcraftfabric.pamassets.artifice.DataResource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static final Path HARVEST_JAR_PATH = FabricLoader.getInstance().getGameDir().resolve("pamhc/harvestcraft.jar");

    public LocalPam() {
        this(HARVEST_JAR_PATH);
    }

    private LocalPam(Path path) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        } else {
            this.gson = new GsonBuilder().create();
        }
        try {
            this.zipFile = new ZipFile(path.toFile());
        } catch (IOException e) {
            Harvestcraftfabric.LOGGER.info("Failed to load local harvestcraft");
            throw new RuntimeException(e);
        }
        this.content = loadPamContent();
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
        builder.addItemTag(modID("seed"), tagBuilder -> Harvestcraftfabric.getInstance().getSeedItems().forEach(tagBuilder::value));
        builder.addItemTag(modID("saplings"), tagBuilder -> Harvestcraftfabric.getInstance().getSaplingItems().forEach(tagBuilder::value));
        builder.addItemTag(modID("buyable"), tagBuilder -> tagBuilder.include(modID("seed")).include(modID("saplings")));


        getContent().getCrops().forEach(crop -> {
            Identifier cropID = modID(Harvestcraftfabric.getCropID(crop));
            Identifier seedID = modID(Harvestcraftfabric.getSeedItemID(crop));
            Identifier itemID = modID(Harvestcraftfabric.getItemID(crop));
            builder.addLootTable(modID("blocks/" + cropID.getPath()), lootTableBuilder -> {
                addGrowableLootTable(cropID, itemID, seedID, lootTableBuilder);
            });
        });

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

    public static Identifier modID(String path) {
        return new Identifier("harvestcraft", path);
    }

    private final Identifier condBlockStateProperty = new Identifier("block_state_property");
    private final Identifier condInverted = new Identifier("inverted");
    private final Identifier funcSetCount = new Identifier("set_count");
    private final Identifier typeBlock = new Identifier("block");
    private final Identifier typeItem = new Identifier("item");
    private final Identifier funcExplosionDecay = new Identifier("explosion_decay");

    private void addGrowableLootTable(Identifier crop, Identifier item, Identifier seed, LootTableBuilder lootTableBuilder) {
        lootTableBuilder
                .type(typeBlock)
                .pool(pool -> pool
                        .rolls(1)
                        .entry(entry -> entry
                                .condition(condBlockStateProperty, condition -> condition
                                        .add("block", crop.toString())
                                        .addObject("properties", jsonObjectBuilder -> jsonObjectBuilder
                                                .add("age", PamCropBlock.MAX_AGE)))
                                .type(typeItem)
                                .name(item)
                                .function(funcSetCount, function -> {
                                    function.addObject("count", jsonObjectBuilder -> jsonObjectBuilder
                                            .add("type", "uniform")
                                            .add("min", 1)
                                            .add("max", 3));
                                })
                        )

                        .entry(entry -> entry
                                .condition(condInverted, condition -> condition
                                        .addObject("term", jsonObjectBuilder -> jsonObjectBuilder
                                                .add("condition", condBlockStateProperty.toString())
                                                .add("block", crop.toString())
                                                .addObject("properties", pjsonObjectBuilder -> pjsonObjectBuilder
                                                        .add("age", PamCropBlock.MAX_AGE))
                                        )
                                )
                                .type(typeItem)
                                .name(seed)))
                .jsonArray("functions", jsonArrayBuilder -> {
                    jsonArrayBuilder.addObject(jsonObjectBuilder -> jsonObjectBuilder.add("function", "explosion_decay"));
                });
    }

    private void registerDatum(Loader loader, ArtificeResourcePack.ServerResourcePackBuilder builder) {
        try {
            InputStream is = zipFile.getInputStream(loader.getEntry());
            byte[] data = loader.getTransformer().transform(IOUtils.toByteArray(is));
            if (data == NO_RESULT_ITEM) {
                Harvestcraftfabric.LOGGER.debug("Not implemented skip: " + loader.getIdentifier());
            } else if (data != null) {
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
        if (AccessorIdentifier.isPathValid(path))
            return new Identifier("harvestcraft", path);
        else return null;
    }

    private static final byte[] NO_RESULT_ITEM = {1, 2, 3, 4};

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
            JsonObject resultObj = element.getAsJsonObject();
            String result = resultObj.getAsJsonPrimitive("item").getAsString();
            if (!Registry.ITEM.getOrEmpty(new Identifier(result)).isPresent()) {
                return NO_RESULT_ITEM;
            }
            if (resultObj.has("data")) {
                try {
                    if (modifyItemObjectWithData(resultObj, result)) {
                        Harvestcraftfabric.LOGGER.error("Tag in output!");
                        return null;
                    }
                } catch (IllegalStateException ex) {
                    Harvestcraftfabric.LOGGER.error("Failed to adjust data element.");
                    return null;
                }
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

    private boolean modifyItemObjectWithData(JsonObject object, String item) throws IllegalStateException {
        int data = object.getAsJsonPrimitive("data").getAsInt();
        object.remove("data");
        NameConversion conversion = NameConversion.fromItemAndData(new Identifier(item), data);
        if (conversion != null) {
            if (conversion.isTag()) {
                object.addProperty("tag", conversion.getValue());
                object.remove("item");
                return true;
            } else {
                object.addProperty("item", conversion.getValue());
                return false;
            }
        }
        throw new IllegalStateException();
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
            if (modifyIngredient(ingredient.getValue().getAsJsonObject()) != IngredientModifyResult.Success)
                return null;
        }
        object.addProperty("type", "minecraft:crafting_shaped");
        return gson.toJson(object).getBytes();
    }

    private IngredientModifyResult modifyIngredient(JsonObject ingred) {

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
            if (ingred.has("data")) {
                try {
                    modifyItemObjectWithData(ingred, ingred.get("item").getAsString());
                } catch (IllegalStateException ex) {
                    Harvestcraftfabric.LOGGER.error("Unknown data: " + ingred.toString());
                    return IngredientModifyResult.DataFieldPresent;
                }
            } else {
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
        }
        if (ingred.has("item")) {
            Identifier initialItem = new Identifier(ingred.getAsJsonPrimitive("item").getAsString());
            if (!Registry.ITEM.getOrEmpty(initialItem).isPresent()) {
                Harvestcraftfabric.LOGGER.error("Missing ingredient: " + initialItem.toString());
                missingIngredients.add(initialItem.toString());
                return IngredientModifyResult.MissingIngredient;
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

    public static void verifyLocalFiles(Path jar) throws MissingHarvestCraftException {
        Harvestcraftfabric.LOGGER.info("Checking local harvestcraft");
        Path jarParent = jar.getParent();
        if (!Files.isDirectory(jarParent)) {
            if (!jarParent.toFile().mkdirs()) {
                throw new MissingHarvestCraftException("Failed to create directory `pamhc`. Is there a file conflict?");
            }
        }
        if (Files.notExists(jar)) {
            throw new MissingHarvestCraftException("Missing harvestcraft.jar. Download Pam's HarvestCraft for 1.12.2 and place it in .minecraft/pamhc/harvestcraft.jar");
        } else if (Files.isDirectory(jar)) {
            throw new MissingHarvestCraftException("Failed to validate existing jar file `pamhc/harvestcraft.jar`. Is there a file conflict?");
        }
    }

    public static final class MissingHarvestCraftException extends Exception {
        public MissingHarvestCraftException(String s) {
            super(s);
        }
    }

    static {
        skipRecipes.add("assets/harvestcraft/recipes/peanutbuttercookiesitem - Copy (2).json");
        skipRecipes.add("assets/harvestcraft/recipes/pizzasliceitem_anchovypepperonipizzaitem_x13 - Copy (2).json");
        skipRecipes.add("assets/harvestcraft/recipes/plainyogurtitem_x4 - Copy.json");
        skipRecipes.add("assets/harvestcraft/recipes/cake.json");
        skipRecipes.add("assets/harvestcraft/recipes/honey.json");
        skipRecipes.add("assets/harvestcraft/recipes/honeyitem_x9_honey.json");
        skipRecipes.add("assets/harvestcraft/recipes/minecraft_pumpkinseeds.json");
        skipRecipes.add("assets/harvestcraft/recipes/minecraft_pumpkinblocks.json");
    }

    private enum IngredientModifyResult {
        Success,
        DataFieldPresent,
        UnhandledOre,
        UnknownType,
        MissingIngredient,
    }

}
