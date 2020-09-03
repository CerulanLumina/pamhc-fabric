package net.cerulan.harvestcraftfabric.trees;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.mixin.event.lifecycle.MinecraftServerMixin;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TreeInitializer {

//    public static ConfiguredFeature<TreeFeatureConfig, ?> TEST_TREE = Feature.TREE.configure(
//                    new TreeFeatureConfig.Builder(
//                            new SimpleBlockStateProvider(Blocks.DARK_OAK_LOG.getDefaultState()),
//                            new SimpleBlockStateProvider(Blocks.ACACIA_LEAVES.getDefaultState()),
//                            new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3),
//                            new StraightTrunkPlacer(4, 2, 0), new TwoLayersFeatureSize(1, 0, 1)
//                    )
//                            .maxWaterDepth(Integer.MAX_VALUE)
//                            .heightmap(Heightmap.Type.MOTION_BLOCKING)
//                            .ignoreVines().build()
//    );

    public static final SaplingGenerator SAPLING_GENERATOR = new DynamicSaplingGenerator();




//    public static void registerTree() {
//        DynamicRegistryManager.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl.Impl impl;
//        Registry<ConfiguredFeature<?, ?>> owo = (Registry<ConfiguredFeature<?, ?>>)Registry.REGISTRIES.get(Registry.CONFIGURED_FEATURE_WORLDGEN.getValue());
//
////        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("harvestcraft", "test_tree"), TEST_TREE);
//        assert owo != null;
//        Registry.register(owo, new Identifier("harvestcraft", "test_tree"), TEST_TREE);
//
//
//        Registry.register(Registry.BLOCK, new Identifier("harvestcraft", "apple_sapling"), block);
////        Registry.register(Registry.CONFIGURED_FEATURE_WORLDGEN,
////                new Identifier("harvestcraft", "test_tree"),
////                ConfiguredFeatures.ACACIA);
//    }

}
