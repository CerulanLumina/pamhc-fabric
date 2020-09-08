package net.cerulan.harvestcraftfabric.block;

import net.cerulan.harvestcraftfabric.worldgen.FruitSaplingGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;

public class FruitSaplingBlock extends SaplingBlock {
    public FruitSaplingBlock(Block fruitBlock) {
        super(new FruitSaplingGenerator(fruitBlock), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    }
}