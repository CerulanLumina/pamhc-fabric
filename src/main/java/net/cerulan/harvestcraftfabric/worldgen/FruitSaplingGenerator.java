package net.cerulan.harvestcraftfabric.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FruitSaplingGenerator extends SaplingGenerator {

    private final Block fruit;
    public FruitSaplingGenerator(Block fruit) {
        this.fruit = fruit;
    }

    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return PamWorldGenerator.getTreeGeneratorForFruitBlock(fruit);
    }

}
