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
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PamWorldGenerator {

    public static BiomeFruitTreeDecorator TREE_DECORATOR;
    public static TreeDecoratorType<FruitTreeDecorator> TREE_DECORATOR_TYPE;

    private static final HashSet<Biome.Category> toAdd = new HashSet<>();

    public static void initWorldGen(ArrayList<PamFruitBlock> fruitBlocks) {
        toAdd.add(Biome.Category.JUNGLE);
        toAdd.add(Biome.Category.FOREST);
        toAdd.add(Biome.Category.EXTREME_HILLS);
        toAdd.add(Biome.Category.PLAINS);
        toAdd.add(Biome.Category.SAVANNA);
        toAdd.add(Biome.Category.SWAMP);
        toAdd.add(Biome.Category.TAIGA);

        setupTrees(fruitBlocks);

        Registry.BIOME.stream().filter(PamWorldGenerator::shouldAddToBiome).forEach(PamWorldGenerator::registerTreeInBiome);

        RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> registerTreeInBiome(biome));
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

    private static boolean shouldAddToBiome(Biome biome) {
        return toAdd.contains(biome.getCategory());
    }

    private static void registerTreeInBiome(Biome biome) {
        RandomFeatureConfig config = new RandomFeatureConfig(ImmutableList.copyOf(treeConfigured.values()).stream().map(a -> a.withChance(0.0001f)).collect(Collectors.toList()), Feature.NO_OP.configure(DefaultFeatureConfig.INSTANCE));
        treeConfigured.values().forEach(tree -> biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR.configure(config)));
    }

}
