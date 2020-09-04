package net.cerulan.harvestcraftfabric.trees;

import net.cerulan.harvestcraftfabric.worldgen.PamWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DynamicSaplingGenerator extends SaplingGenerator {

    private final Block fruit;
    public DynamicSaplingGenerator(Block fruit) {
        this.fruit = fruit;
    }

    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return PamWorldGenerator.getTreeGeneratorForFruitBlock(fruit);
    }

}
