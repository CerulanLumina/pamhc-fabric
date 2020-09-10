package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.ShippingBinBE;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class ShippingBinBlock extends MarketBlock {

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ShippingBinBE();
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ShippingBinBE) {
            ShippingBinBE bin = (ShippingBinBE) be;
            return bin.getInventory();
        } else return null;
    }
}
