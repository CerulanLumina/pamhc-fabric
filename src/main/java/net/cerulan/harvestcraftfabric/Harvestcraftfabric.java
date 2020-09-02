package net.cerulan.harvestcraftfabric;

import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.block.PamCropBlock;
import net.cerulan.harvestcraftfabric.item.PamSeedItem;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
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

    @Override
    public void onInitialize() {
        this.localPam = new LocalPam();
        INSTANCE = this;

        localPam.getContent().getTools().forEach(tool -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", tool + "item"), item);
        });

        localPam.getContent().getBasicItems().forEach(itemName -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", itemName + "item"), item);
        });

        FoodComponent basic = new FoodComponent.Builder().hunger(1).saturationModifier(0.0f).build();

        localPam.getContent().getCrops().forEach(name -> {
            PamCropBlock block = new PamCropBlock();
            Item item = new AliasedBlockItem(block, new Item.Settings().group(HARVESTCRAFT_CROP_GROUP).food(basic));
            Item seedItem = new PamSeedItem(block, new Item.Settings().group(HARVESTCRAFT_CROP_GROUP));

            CROP_BLOCKS.add(block);

            Registry.register(Registry.ITEM, new Identifier("harvestcraft", getSeedItemID(name)), seedItem);
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", getItemID(name)), item);
            Registry.register(Registry.BLOCK, new Identifier("harvestcraft", getCropID(name)), block);
        });

        localPam.getContent().getFoods().forEach(food -> {
            // TODO food component
            Item foodItem = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP).food(FoodComponents.APPLE));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", food + "item"), foodItem);
        });

        localPam.getContent().getIngredients().forEach(ingred -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_FOOD_GROUP).food(basic));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", ingred + "item"), item);
        });

        localPam.getContent().getFruits().forEach(fruit -> {
            Item item = new Item(new Item.Settings().group(HARVESTCRAFT_CROP_GROUP).food(basic));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", fruit + "item"), item);
        });

        Artifice.registerData(new Identifier("harvestcraft", "harvestcraft"), localPam::registerPamData);
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
