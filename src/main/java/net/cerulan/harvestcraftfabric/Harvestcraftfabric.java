package net.cerulan.harvestcraftfabric;

import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.block.PamCropBlock;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.config.FoodsConfig;
import net.cerulan.harvestcraftfabric.item.DrinkFoodItem;
import net.cerulan.harvestcraftfabric.item.PamSeedItem;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.cerulan.harvestcraftfabric.trees.TestSaplingBlock;
import net.cerulan.harvestcraftfabric.trees.TreeInitializer;
import net.cerulan.harvestcraftfabric.worldgen.PamWorldGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public final class Harvestcraftfabric implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("PamHC-Fabric");
    private LocalPam localPam;
    private static Harvestcraftfabric INSTANCE = null;
    public static ItemGroup HARVESTCRAFT_CROP_GROUP = FabricItemGroupBuilder.build(
            new Identifier("harvestcraft", "crops"),
            () -> new ItemStack(Registry.ITEM.get(new Identifier("harvestcraft", "tomatoitem"))));
    public static ItemGroup HARVESTCRAFT_FOOD_GROUP = FabricItemGroupBuilder.build(
            new Identifier("harvestcraft", "food"),
            () -> new ItemStack(Registry.ITEM.get(new Identifier("harvestcraft", "mcpamitem"))));

    public static final ArrayList<PamCropBlock> CROP_BLOCKS = new ArrayList<>();
    public static final ArrayList<TestSaplingBlock> SAPLING_BLOCKS = new ArrayList<>();
    public static final ArrayList<Identifier> SEED_ITEMS = new ArrayList<>();

    @Override
    public void onInitialize() {
        this.localPam = new LocalPam();
        INSTANCE = this;

        ConfigHandler config = ConfigHandler.loadConfig();

        if (config == null) {
            Harvestcraftfabric.LOGGER.error("Failed to load config.");
            return;
        }

        FoodsConfig foodsConfig = config.getFoodsConfig();
        FoodComponent cropResultFood = foodsConfig.getCropResultFood().toFoodComponent();

        // Tools
        localPam.getContent().getTools().forEach(tool -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", tool + "item"), item);
        });

        // General Items
        localPam.getContent().getBasicItems().forEach(itemName -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", itemName + "item"), item);
        });

        // Crops
        localPam.getContent().getCrops().forEach(name -> {
            PamCropBlock block = new PamCropBlock();
            Item item = new AliasedBlockItem(block, new Item.Settings().group(HARVESTCRAFT_CROP_GROUP).food(cropResultFood));
            Item seedItem = new PamSeedItem(block, new Item.Settings().group(HARVESTCRAFT_CROP_GROUP));

            CROP_BLOCKS.add(block);

            Identifier seedID = new Identifier("harvestcraft", getSeedItemID(name));
            SEED_ITEMS.add(seedID);

            Registry.register(Registry.ITEM, seedID, seedItem);
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", getItemID(name)), item);
            Registry.register(Registry.BLOCK, new Identifier("harvestcraft", getCropID(name)), block);
        });

        // Foods
        localPam.getContent().getFoods().forEach(food -> {
            Identifier foodId = new Identifier("harvestcraft", food + "item");
            FoodsConfig.FoodObject obj = foodsConfig.getFood(foodId.getPath());
            FoodComponent foodComponent = obj.toFoodComponent();
            Item.Settings settings = new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP).food(foodComponent);
            Item foodItem;
            if (obj.isDrink()) {
                foodItem = new DrinkFoodItem(settings);
            } else {
                foodItem = new Item(settings);
            }
            Registry.register(Registry.ITEM, foodId, foodItem);
        });
        foodsConfig.validateConfig(localPam.getContent().getFoods());

        // Ingredients
        localPam.getContent().getIngredients().forEach(ingred -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", ingred + "item"), item);
        });



        // Fruits
        localPam.getContent().getFruits().forEach(fruit -> {

            // TODO trees
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_CROP_GROUP).food(cropResultFood));
            TestSaplingBlock saplingBlock = new TestSaplingBlock();
            Item sapling = new BlockItem(saplingBlock, new Item.Settings().group(HARVESTCRAFT_CROP_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", fruit + "item"), item);
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", fruit + "_sapling"), sapling);
            Registry.register(Registry.BLOCK, new Identifier("harvestcraft", fruit + "_sapling"), saplingBlock);
            SAPLING_BLOCKS.add(saplingBlock);
        });

        Artifice.registerData(new Identifier("harvestcraft", "harvestcraft"), localPam::registerPamData);
        PamWorldGenerator.initWorldGen();
    }

    public LocalPam getLocalPam() {
        return localPam;
    }
    public static Harvestcraftfabric getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("Harvestcraft has not been initialized yet");
        }
        return INSTANCE;
    }

    public static String getSeedItemID(String cropName) {
        switch (cropName) {
            case "coffeebean":
                return "coffeeseeditem";
            case "mustardseeds":
                return "mustardseeditem";
            case "tealeaf":
                return "teaseeditem";
            default:
                return cropName + "seeditem";
        }
    }

    public static String getCropID(String cropName) {
        return "pam" + cropName + "crop";
    }

    public static String getItemID(String cropName) {
        return cropName + "item";
    }
}
