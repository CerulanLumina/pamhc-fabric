package net.cerulan.harvestcraftfabric.trees;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DynamicSaplingGenerator extends SaplingGenerator {

    private String saplingKey;
    public DynamicSaplingGenerator(String saplingKey) {
        this.saplingKey = saplingKey;
    }

    public static Map<String, ConfiguredFeature<TreeFeatureConfig, ?>> config = new HashMap<>();

    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return config.get(saplingKey);
    }

}
