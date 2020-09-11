package net.cerulan.harvestcraftfabric;

import com.google.common.collect.ImmutableList;
import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.block.*;
import net.cerulan.harvestcraftfabric.block.machine.MachineRegistry;
import net.cerulan.harvestcraftfabric.blockentity.MarketBlockEntity;
import net.cerulan.harvestcraftfabric.blockentity.ModBlockEntities;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.config.FoodsConfig;
import net.cerulan.harvestcraftfabric.gui.GuiRegistry;
import net.cerulan.harvestcraftfabric.gui.MarketGuiDescription;
import net.cerulan.harvestcraftfabric.item.DrinkFoodItem;
import net.cerulan.harvestcraftfabric.item.PamSeedItem;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.cerulan.harvestcraftfabric.worldgen.PamWorldGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.*;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public final class Harvestcraftfabric implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("PamHC-Fabric");
    private LocalPam localPam;
    private static Harvestcraftfabric INSTANCE = null;
    public final ItemGroup harvestcraftCropGroup = FabricItemGroupBuilder.build(
            new Identifier("harvestcraft", "crops"),
            () -> new ItemStack(Registry.ITEM.get(new Identifier("harvestcraft", "tomatoitem"))));
    public final ItemGroup harvestcraftFoodGroup = FabricItemGroupBuilder.build(
            new Identifier("harvestcraft", "food"),
            () -> new ItemStack(Registry.ITEM.get(new Identifier("harvestcraft", "mcpamitem"))));

    private List<PamCropBlock> cropBlocks = new ArrayList<>();
    private List<PamFruitBlock> fruitBlocks = new ArrayList<>();
    private List<PamLogBlock> logBlocks = new ArrayList<>();
    private List<SaplingBlock> saplingBlocks = new ArrayList<>();
    private List<PamGardenBlock> gardenBlocks = new ArrayList<>();
    private List<Identifier> seedItems = new ArrayList<>();
    private List<Identifier> saplingItems = new ArrayList<>();
    private List<Identifier> grownItems = new ArrayList<>();

    private FoodComponent cropResultFood = null;

    public static class Packets {
        public static final Identifier SET_MARKET_STACK = LocalPam.modID("set_market_stack");

        public static void registerC2SPackets() {
            ServerSidePacketRegistry.INSTANCE.register(SET_MARKET_STACK, Packets::acceptSetMarketStack);
        }

        private static void acceptSetMarketStack(PacketContext context, PacketByteBuf buf) {
            BlockPos pos = buf.readBlockPos();
            ItemStack stack = buf.readItemStack();
            Tag<Item> buyable = TagRegistry.item(MarketGuiDescription.buyable);
            context.getTaskQueue().execute(() -> {
                World world = context.getPlayer().world;
                if (world.canSetBlock(pos) && buyable.contains(stack.getItem()) && world.getBlockState(pos).isOf(MachineRegistry.MARKET_BLOCK)) {
                    MarketBlockEntity market = (MarketBlockEntity)world.getBlockEntity(pos);
                    if (market != null)
                        market.setOutput(stack);
                }
            });
        }
    }

    @Override
    public void onInitialize() {

        ConfigHandler config = ConfigHandler.loadConfig();

        this.localPam = new LocalPam();
        INSTANCE = this;

        if (config == null) {
            Harvestcraftfabric.LOGGER.error("Failed to load config.");
            return;
        }

        FoodsConfig foodsConfig = config.getFoodsConfig();
        cropResultFood = foodsConfig.getCropResultFood().toFoodComponent();

        // Tools
        localPam.getContent().getTools().forEach(tool -> {
            Item item = new Item(new Item.Settings().group(harvestcraftFoodGroup));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", tool + "item"), item);
        });

        // General Items
        localPam.getContent().getBasicItems().forEach(itemName -> {
            Item item = new Item(new Item.Settings().group(harvestcraftFoodGroup));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", itemName + "item"), item);
        });

        // Crops
        localPam.getContent().getCrops().forEach(name -> {
            PamCropBlock block = new PamCropBlock();
            Item item = new AliasedBlockItem(block, new Item.Settings().group(harvestcraftCropGroup).food(cropResultFood));
            Item seedItem = new PamSeedItem(block, new Item.Settings().group(harvestcraftCropGroup));

            block.setSeedsItem(seedItem);

            cropBlocks.add(block);

            Identifier seedID = new Identifier("harvestcraft", getSeedItemID(name));
            seedItems.add(seedID);

            Identifier itemID = LocalPam.modID(getItemID(name));
            grownItems.add(itemID);

            Registry.register(Registry.ITEM, seedID, seedItem);
            Registry.register(Registry.ITEM, itemID, item);
            Registry.register(Registry.BLOCK, new Identifier("harvestcraft", getCropID(name)), block);
        });

        MachineRegistry.registerMachines();
        ModBlockEntities.registerBlockEntities();

        GuiRegistry.registerScreenHandlers();

        // Foods
        localPam.getContent().getFoods().forEach(food -> {
            Identifier foodId = new Identifier("harvestcraft", food + "item");
            FoodsConfig.FoodObject obj = foodsConfig.getFood(foodId.getPath());
            FoodComponent foodComponent = obj.toFoodComponent();
            Item.Settings settings = new Item.Settings().group(harvestcraftFoodGroup).food(foodComponent);
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
            Item item = new Item(new Item.Settings().group(harvestcraftFoodGroup));
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", ingred + "item"), item);
        });

        // Fruits
        registerFruitBlockAndSapling("apple", new Identifier("apple"));
        localPam.getContent().getFruits().forEach(this::registerPamFruit);

        // Log Trees
        localPam.getContent().getLogTrees().forEach(tree -> {
            PamLogBlock logBlock = new PamLogBlock(tree);
            WoodSaplingBlock saplingBlock = new WoodSaplingBlock(logBlock);
            Item saplingItem = new BlockItem(saplingBlock, new Item.Settings().group(harvestcraftCropGroup));
            Item logBlockItem = new BlockItem(logBlock, new Item.Settings().group(harvestcraftCropGroup));
            Identifier saplingID = new Identifier("harvestcraft", tree + "_sapling");
            Identifier logID = new Identifier("harvestcraft", "pam" + tree);
            Registry.register(Registry.BLOCK, logID, logBlock);
            Registry.register(Registry.BLOCK, saplingID, saplingBlock);
            Registry.register(Registry.ITEM, saplingID, saplingItem);
            Registry.register(Registry.ITEM, logID, logBlockItem);
            logBlocks.add(logBlock);
            saplingBlocks.add(saplingBlock);
        });


        localPam.getContent().getCake().forEach(cake -> {
            PamCakeBlock block = new PamCakeBlock();
            Item item = new BlockItem(block, new Item.Settings().group(harvestcraftFoodGroup));
            Identifier blockID;
            if (cake.endsWith("cake")) blockID = new Identifier("harvestcraft", cake);
            else blockID = new Identifier("harvestcraft", cake + "cake");
            Registry.register(Registry.BLOCK, blockID, block);
            Registry.register(Registry.ITEM, new Identifier("harvestcraft", cake + "item"), item);
        });

        localPam.getContent().getGardens().forEach((garden, categories) -> {
            PamGardenBlock gardenBlock = new PamGardenBlock();
            Registry.register(Registry.BLOCK, new Identifier("harvestcraft", garden + "garden"), gardenBlock);
            categories.forEach(cat -> PamWorldGenerator.registerGardenForCategoryString(cat, gardenBlock));
            gardenBlocks.add(gardenBlock);
        });

        RecipeRegistry.regiterRecipeHandlers();

        gardenBlocks = ImmutableList.copyOf(gardenBlocks);
        cropBlocks = ImmutableList.copyOf(cropBlocks);
        fruitBlocks = ImmutableList.copyOf(fruitBlocks);
        logBlocks = ImmutableList.copyOf(logBlocks);
        saplingBlocks = ImmutableList.copyOf(saplingBlocks);
        seedItems = ImmutableList.copyOf(seedItems);
        saplingItems = ImmutableList.copyOf(saplingItems);
        grownItems = ImmutableList.copyOf(grownItems);

        Artifice.registerData(new Identifier("harvestcraft", "harvestcraft"), localPam::registerPamData);
        PamWorldGenerator.initWorldGen(fruitBlocks, logBlocks);
        Packets.registerC2SPackets();

        LootTableLoadingCallback.EVENT.register(this::addSeedItemsToGrass);
    }

    private static final HashSet<Identifier> grassLootTables = new HashSet<>();
    static {
        grassLootTables.add(new Identifier("blocks/fern"));
        grassLootTables.add(new Identifier("blocks/grass"));
        grassLootTables.add(new Identifier("blocks/large_fern"));
        grassLootTables.add(new Identifier("blocks/tall_grass"));
    }
    public void addSeedItemsToGrass(ResourceManager resourceManager, LootManager manager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter setter) {
        if (grassLootTables.contains(id)) {
            if (ConfigHandler.getGeneralConfig().addCropSeedsToGrass)
                supplier.pool(new LootPool.Builder().conditionally(RandomChanceLootCondition.builder(0.0625f)).with(TagEntry.builder(TagRegistry.item(LocalPam.modID("seed")))));
        }
    }

    private void registerPamFruit(String fruit) {
        Item pamFruitItem = new Item(new Item.Settings().group(harvestcraftFoodGroup).food(cropResultFood));
        Identifier fruitItemID = new Identifier("harvestcraft", fruit + "item");
        grownItems.add(fruitItemID);
        registerFruitBlockAndSapling(fruit, fruitItemID);
        Registry.register(Registry.ITEM, fruitItemID, pamFruitItem);
    }

    private void registerFruitBlockAndSapling(String fruit, Identifier fruitItemID) {
        PamFruitBlock fruitBlock = new PamFruitBlock(fruitItemID);
        FruitSaplingBlock saplingBlock = new FruitSaplingBlock(fruitBlock);
        Item sapling = new BlockItem(saplingBlock, new Item.Settings().group(harvestcraftCropGroup));
        Identifier saplingID = LocalPam.modID(fruit + "_sapling");
        Registry.register(Registry.ITEM, saplingID, sapling);
        Registry.register(Registry.BLOCK, saplingID, saplingBlock);
        Registry.register(Registry.BLOCK, new Identifier("harvestcraft", "pam" + fruit), fruitBlock);
        saplingBlocks.add(saplingBlock);
        fruitBlocks.add(fruitBlock);
        saplingItems.add(saplingID);
        HarvestcraftContent.registerFruitTree(new HarvestcraftContent.FruitTree(sapling, () -> new ItemStack(Registry.ITEM.get(fruitItemID)), fruitBlock));
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

    public Stream<Block> getCutoutBlocks() {
        Stream<Block> stream = cropBlocks.stream().map(b -> b);
        stream = Stream.concat(stream, saplingBlocks.stream());
        stream = Stream.concat(stream, fruitBlocks.stream());
        stream = Stream.concat(stream, gardenBlocks.stream());
        return stream;
    }

    public List<Identifier> getSeedItems() {
        return seedItems;
    }

    public List<Identifier> getSaplingItems() {
        return saplingItems;
    }

    public List<Identifier> getGrownItems() {
        return grownItems;
    }
}
