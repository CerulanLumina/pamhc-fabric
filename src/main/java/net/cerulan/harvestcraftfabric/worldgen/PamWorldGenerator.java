package net.cerulan.harvestcraftfabric.worldgen;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.mixin.AccessorTreeDecoratorType;
import net.cerulan.harvestcraftfabric.trees.DynamicSaplingGenerator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.tree.TreeDecoratorType;

import java.util.HashSet;

public class PamWorldGenerator {

    public static BiomeFruitTreeDecorator TREE_DECORATOR;
    public static TreeDecoratorType<FruitTreeDecorator> TREE_DECORATOR_TYPE;

    public static void initWorldGen() {
        setupTrees();
    }

    private static final HashSet<Biome> checkBiomes = new HashSet<>();

    private static void setupTrees() {
        TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("harvestcraft", "fruit_tree"), new BiomeFruitTreeDecorator(ChanceDecoratorConfig.CODEC));
        TREE_DECORATOR_TYPE = AccessorTreeDecoratorType.register("harvestcraft:fruit", FruitTreeDecorator.CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register(PamWorldGenerator::setTreeFeature);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(PamWorldGenerator::setTreeFeature);
    }

    private static void setTreeFeature(MinecraftServer serverInstance, Object... iDontReallyCare) {
        DynamicRegistryManager registryManager = serverInstance.getRegistryManager();
        Registry<?> registry = registryManager.get(Registry.CONFIGURED_FEATURE_WORLDGEN);
        DynamicSaplingGenerator.config.clear();
        Harvestcraftfabric.getInstance().getLocalPam().getContent().getFruits().forEach(fruit -> {
            DynamicSaplingGenerator.config.put(fruit, (ConfiguredFeature<TreeFeatureConfig, ?>)registry.get(new Identifier("harvestcraft", fruit + "_tree")));
        });
    }


}
