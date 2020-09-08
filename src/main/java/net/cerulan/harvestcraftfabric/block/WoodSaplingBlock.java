package net.cerulan.harvestcraftfabric.block;

import net.cerulan.harvestcraftfabric.worldgen.WoodSaplingGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;

public class WoodSaplingBlock extends SaplingBlock {
    public WoodSaplingBlock(Block logBlock) {
        super(new WoodSaplingGenerator(logBlock), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
    }
}
