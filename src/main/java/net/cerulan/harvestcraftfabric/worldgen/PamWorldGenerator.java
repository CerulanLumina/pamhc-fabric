package net.cerulan.harvestcraftfabric.worldgen;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.block.PamFruitBlock;
import net.cerulan.harvestcraftfabric.mixin.AccessorTreeDecoratorType;
import net.cerulan.harvestcraftfabric.trees.DynamicSaplingGenerator;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.TreeDecoratorType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PamWorldGenerator {

    public static BiomeFruitTreeDecorator TREE_DECORATOR;
    public static TreeDecoratorType<FruitTreeDecorator> TREE_DECORATOR_TYPE;

    public static void initWorldGen(ArrayList<PamFruitBlock> fruitBlocks) {
        setupTrees(fruitBlocks);
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

//        ServerLifecycleEvents.SERVER_STARTED.register(PamWorldGenerator::setTreeFeature);
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(PamWorldGenerator::setTreeFeature);
    }

    public static ConfiguredFeature<TreeFeatureConfig, ?> getTreeGeneratorForFruitBlock(Block block) {
        return treeConfigured.get(block);
    }

    private static void setTreeFeature(MinecraftServer serverInstance, Object... iDontReallyCare) {
//        DynamicRegistryManager registryManager = serverInstance.getRegistryManager();
//        Registry<?> registry = registryManager.get(Registry.CONFIGURED_FEATURE_WORLDGEN);
//        DynamicSaplingGenerator.config.clear();
//        Harvestcraftfabric.getInstance().getLocalPam().getContent().getFruits().forEach(fruit -> {
//            DynamicSaplingGenerator.config.put(fruit, (ConfiguredFeature<TreeFeatureConfig, ?>)registry.get(new Identifier("harvestcraft", fruit + "_tree")));
//        });
    }


}
