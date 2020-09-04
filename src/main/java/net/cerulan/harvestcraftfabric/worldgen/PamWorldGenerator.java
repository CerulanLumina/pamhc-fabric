package net.cerulan.harvestcraftfabric.worldgen;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.block.PamFruitBlock;
import net.cerulan.harvestcraftfabric.mixin.AccessorTreeDecoratorType;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NoiseHeightmapDecoratorConfig;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.ForestFlowerBlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PamWorldGenerator {

    public static BiomeFruitTreeDecorator TREE_DECORATOR;
    public static TreeDecoratorType<FruitTreeDecorator> TREE_DECORATOR_TYPE;

    private static final HashSet<Biome.Category> addTrees = new HashSet<>();
    private static final HashMap<Biome.Category, Block> addGardensForBiomes = new HashMap<>();

    public static void registerGardenForCategoryString(String category, Block block) {
        Biome.Category cat = Biome.Category.method_28424(category);
        assert cat != null;
        addGardensForBiomes.put(cat, block);
    }

    public static void initWorldGen(ArrayList<PamFruitBlock> fruitBlocks) {
        addTrees.add(Biome.Category.JUNGLE);
        addTrees.add(Biome.Category.FOREST);
        addTrees.add(Biome.Category.EXTREME_HILLS);
        addTrees.add(Biome.Category.PLAINS);
        addTrees.add(Biome.Category.SAVANNA);
        addTrees.add(Biome.Category.SWAMP);
        addTrees.add(Biome.Category.TAIGA);

        setupTrees(fruitBlocks);

        Registry.BIOME.stream()
                .filter(biome -> !checkBiomes.contains(biome))
                .forEach(PamWorldGenerator::registerFeaturesForBiome);

        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> {
            registerFeaturesForBiome(biome);
        });
    }

    private static final HashSet<Biome> checkBiomes = new HashSet<>();
    private static final HashMap<Block, ConfiguredFeature<TreeFeatureConfig, ?>> treeConfigured = new HashMap<>();

    private static void setupTrees(ArrayList<PamFruitBlock> fruitBlocks) {
        TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("harvestcraft", "fruit_tree"), new BiomeFruitTreeDecorator(ChanceDecoratorConfig.field_24980));
        TREE_DECORATOR_TYPE = AccessorTreeDecoratorType.register("harvestcraft:fruit", FruitTreeDecorator.CODEC);

        fruitBlocks.stream().map(Block::getDefaultState).forEach(state -> {
            ConfiguredFeature<TreeFeatureConfig, ?> feature = Feature.TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG.setTreeDecorators(ImmutableList.of(new FruitTreeDecorator(state))));
            treeConfigured.put(state.getBlock(), feature);
        });

    }

    public static ConfiguredFeature<TreeFeatureConfig, ?> getTreeGeneratorForFruitBlock(Block block) {
        return treeConfigured.get(block);
    }

    private static boolean shouldAddTrees(Biome biome) {
        return addTrees.contains(biome.getCategory());
    }

    private static void registerFeaturesForBiome(Biome biome) {
        if (checkBiomes.contains(biome)) return;
        checkBiomes.add(biome);
        if (shouldAddTrees(biome)) registerTreesForBiome(biome);
        registerGardensForBiome(biome);
    }

    private static void registerTreesForBiome(Biome biome) {
        RandomFeatureConfig config = new RandomFeatureConfig(ImmutableList.copyOf(treeConfigured.values()).stream().map(a -> a.withChance(0.0001f)).collect(Collectors.toList()), Feature.NO_OP.configure(DefaultFeatureConfig.INSTANCE));
        treeConfigured.values().forEach(tree -> biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(config)));
    }

    private static void registerGardensForBiome(Biome biome) {
        Biome.Category category = biome.getCategory();
        if (addGardensForBiomes.containsKey(category)) {
            Block block = addGardensForBiomes.get(category);
            RandomPatchFeatureConfig config = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(block.getDefaultState()), SimpleBlockPlacer.field_24871)).tries(64).build();
            biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.FLOWER.configure(config).createDecoratedFeature(Decorator.NOISE_HEIGHTMAP_32.configure(new NoiseHeightmapDecoratorConfig(-0.8D, 15, 4))));
        }
    }
}
