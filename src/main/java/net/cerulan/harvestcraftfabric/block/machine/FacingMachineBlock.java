package net.cerulan.harvestcraftfabric.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

import java.util.function.Supplier;

public class FacingMachineBlock<T extends BlockEntity> extends MachineBlock<T> {

    public FacingMachineBlock(Supplier<T> beSupplier) {
        super(beSupplier);
        setDefaultState(getDefaultState().with(MachineRegistry.FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MachineRegistry.FACING_PROPERTY);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(MachineRegistry.FACING_PROPERTY, ctx.getPlayerFacing().getOpposite());
    }
}
