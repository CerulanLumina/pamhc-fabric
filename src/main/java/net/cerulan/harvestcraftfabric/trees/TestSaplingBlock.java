package net.cerulan.harvestcraftfabric.trees;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;

public class TestSaplingBlock extends SaplingBlock {
    public TestSaplingBlock(String fruit) {
        super(new DynamicSaplingGenerator(fruit), FabricBlockSettings.copyOf(Blocks.DARK_OAK_SAPLING));
    }
}