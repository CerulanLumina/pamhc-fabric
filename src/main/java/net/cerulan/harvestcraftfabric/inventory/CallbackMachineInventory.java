package net.cerulan.harvestcraftfabric.inventory;

import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class CallbackMachineInventory extends MachineInventory {

    private Consumer<Integer> onChange = null;
    private Consumer<Integer> onRemove = null;

    public CallbackMachineInventory(int count, int[] outputSlots, int[] inputSlots) {
        super(count, outputSlots, inputSlots);
    }

    public void setOnChangeListener(Consumer<Integer> onChange) {
        this.onChange = onChange;
    }

    public void setOnRemoveListener(Consumer<Integer> onRemove) {
        this.onRemove = onRemove;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        if (onChange != null) onChange.accept(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack res = super.removeStack(slot);
        if (onRemove != null) onRemove.accept(slot);
        return res;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack res = super.removeStack(slot, amount);
        if (onRemove != null) onRemove.accept(slot);
        return res;
    }
}
