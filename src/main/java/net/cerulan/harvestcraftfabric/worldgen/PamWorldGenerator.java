package net.cerulan.harvestcraftfabric.worldgen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.mixin.AccessorTreeDecoratorType;
import net.cerulan.harvestcraftfabric.trees.DynamicSaplingGenerator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.tree.TreeDecoratorType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class PamWorldGenerator {

    public static BiomeFruitTreeDecorator BIOME_TREE_DECORATOR;
    public static TreeDecoratorType<FruitTreeDecorator> TREE_DECORATOR_TYPE;

    public static void initWorldGen() {
        toAdd.add(Biome.Category.JUNGLE);
        toAdd.add(Biome.Category.FOREST);
        toAdd.add(Biome.Category.EXTREME_HILLS);
        toAdd.add(Biome.Category.PLAINS);
        toAdd.add(Biome.Category.SAVANNA);
        toAdd.add(Biome.Category.SWAMP);
        toAdd.add(Biome.Category.TAIGA);

        setupTrees();
    }

    private static final HashSet<Biome.Category> toAdd = new HashSet<>();
    private static final HashSet<Biome> checkBiomes = new HashSet<>();

    private static void setupTrees() {
        BIOME_TREE_DECORATOR = Registry.register(Registry.DECORATOR, new Identifier("harvestcraft", "fruit_tree"), new BiomeFruitTreeDecorator(ChanceDecoratorConfig.CODEC));
        TREE_DECORATOR_TYPE = AccessorTreeDecoratorType.register("harvestcraft:fruit", FruitTreeDecorator.CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register(PamWorldGenerator::setTreeFeature);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(PamWorldGenerator::setTreeFeature);
        RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> {
            Harvestcraftfabric.LOGGER.info("here: " + identifier);
        });
    }



    private static void populateBiome(Biome biome) {
        if (checkBiomes.contains(biome)) return;
        checkBiomes.add(biome);
        Biome.Category cat = biome.getCategory();
        if (toAdd.contains(cat)) {
//            addFeature(biome, new Identifier(""), GenerationStep.Feature.VEGETAL_DECORATION, DynamicSaplingGenerator.config.);
        }
    }

    private static void setTreeFeature(MinecraftServer serverInstance, Object... iDontReallyCare) {
        DynamicRegistryManager registryManager = serverInstance.getRegistryManager();
        Registry<?> registry = registryManager.get(Registry.CONFIGURED_FEATURE_WORLDGEN);
        DynamicSaplingGenerator.config.clear();
        Harvestcraftfabric.getInstance().getLocalPam().getContent().getFruits().forEach(fruit -> {
            DynamicSaplingGenerator.config.put(fruit, (ConfiguredFeature<TreeFeatureConfig, ?>)registry.get(new Identifier("harvestcraft", fruit + "_tree")));
        });
    }

    private static void addFeature(Biome biome, Identifier identifier, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
        List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.getGenerationSettings().getFeatures();

        int stepIndex = feature.ordinal();

        while (features.size() <= stepIndex) {
            features.add(Lists.newArrayList());
        }

        List<Supplier<ConfiguredFeature<?, ?>>> stepList = features.get(feature.ordinal());
        if (stepList instanceof ImmutableList) {
            features.set(feature.ordinal(), stepList = new ArrayList<>(stepList));
        }

        if (!BuiltinRegistries.CONFIGURED_FEATURE.getKey(configuredFeature).isPresent()) {
            if (BuiltinRegistries.CONFIGURED_FEATURE.getOrEmpty(identifier).isPresent()) {
                throw new RuntimeException("Duplicate feature: " + identifier.toString());
            }

            BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
        }

        stepList.add(() -> configuredFeature);
    }


}
