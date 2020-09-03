package net.cerulan.harvestcraftfabric.trees;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DynamicSaplingGenerator extends SaplingGenerator {

    public static ConfiguredFeature<TreeFeatureConfig, ?> config = null;

    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return config;
    }

}
