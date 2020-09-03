package net.cerulan.harvestcraftfabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

public class PamFruitBlock extends Block {

    public static final Property<Integer> AGE_PROPERTY = IntProperty.of("age", 0, 2);

    public PamFruitBlock() {
        super(FabricBlockSettings.copyOf(Blocks.COCOA));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE_PROPERTY);
    }
}
