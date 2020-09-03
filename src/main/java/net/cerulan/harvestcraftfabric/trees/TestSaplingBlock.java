package net.cerulan.harvestcraftfabric.trees;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;

import static net.cerulan.harvestcraftfabric.trees.TreeInitializer.SAPLING_GENERATOR;

public class TestSaplingBlock extends SaplingBlock {
    public TestSaplingBlock() {
        super(SAPLING_GENERATOR, FabricBlockSettings.copyOf(Blocks.DARK_OAK_SAPLING));
    }
}