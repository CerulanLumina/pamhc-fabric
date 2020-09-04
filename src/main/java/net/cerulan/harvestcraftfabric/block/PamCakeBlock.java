package net.cerulan.harvestcraftfabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;

public class PamCakeBlock extends CakeBlock {
    public PamCakeBlock() {
        super(FabricBlockSettings.copyOf(Blocks.CAKE));
    }
}
