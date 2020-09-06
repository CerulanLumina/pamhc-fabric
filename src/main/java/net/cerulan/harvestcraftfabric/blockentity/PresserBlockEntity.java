package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.fabricmc.fabric.impl.tag.extension.FabricTagHooks;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class PresserBlockEntity extends BlockEntity implements Tickable, InventoryProvider {

    private final MachineInventory inventory;

    public PresserBlockEntity() {
        super(ModBlockEntities.PRESSER_BE_TYPE);
        inventory = new MachineInventory();
    }

    @Override
    public void tick() {

    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return inventory;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invTag = new CompoundTag();
        Inventories.toTag(invTag, inventory.getItems());
        tag.put("items", invTag);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (tag.contains("items", 10))
            Inventories.fromTag(tag.getCompound("items"), inventory.getItems());
    }
}
