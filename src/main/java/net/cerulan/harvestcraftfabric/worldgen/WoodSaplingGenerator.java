package net.cerulan.harvestcraftfabric.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.Random;

public class WoodSaplingGenerator extends SaplingGenerator {
    private final Block log;
    public WoodSaplingGenerator(Block log) {
        this.log = log;
    }

    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return PamWorldGenerator.getTreeGeneratorForLogBlock(log);
    }
}
