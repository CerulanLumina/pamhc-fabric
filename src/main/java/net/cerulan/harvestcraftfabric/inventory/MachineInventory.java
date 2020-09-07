package net.cerulan.harvestcraftfabric.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import java.util.Arrays;

public class MachineInventory implements SidedInventory {

    private final DefaultedList<ItemStack> items;
    private final int[] outputSlots;
    private final int[] inputSlots;

    public MachineInventory(int count, int[] outputSlots, int[] inputSlots) {
        this.items = DefaultedList.ofSize(count, ItemStack.EMPTY);
        this.outputSlots = Arrays.copyOf(outputSlots, outputSlots.length);
        this.inputSlots = Arrays.copyOf(inputSlots, inputSlots.length);

        if (Arrays.stream(outputSlots).anyMatch(i -> i >= count || i < 0))
            throw new IndexOutOfBoundsException("Output slot array has slot index out of bounds for inventory size");
        if (Arrays.stream(inputSlots).anyMatch(i -> i >= count || i < 0))
            throw new IndexOutOfBoundsException("Input slot array has slot index out of bounds for inventory size");
    }


    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) return this.outputSlots;
        else return this.inputSlots;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return slot == 0 && dir != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot > 0 && dir == Direction.DOWN;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(items, slot, amount);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = Inventories.removeStack(items, slot);
        markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) stack.setCount(getMaxCountPerStack());
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
    }
}
