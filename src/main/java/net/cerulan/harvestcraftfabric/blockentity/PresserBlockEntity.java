package net.cerulan.harvestcraftfabric.blockentity;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.item.ItemStack;

public class PresserBlockEntity extends MachineBlockEntity<DoubleOutputRecipe> {

    /*
    Down - output
    Other - input
     */
    private static final int[] OUTPUT_SLOTS = new int[] {1, 2};
    private static final int[] INPUT_SLOT = new int[] {0};

    public PresserBlockEntity() {
        super(ModBlockEntities.PRESSER_BE_TYPE, RecipeRegistry.PRESSER, new MachineInventory(3, OUTPUT_SLOTS, INPUT_SLOT));
    }

    @Override
    protected int getMaximumProgress() {
        return 125;
    }

    @Override
    protected boolean canOutput(DoubleOutputRecipe recipe) {
        return canOutput(recipe.getOutput(), 1) && canOutput(recipe.getOutput2(), 2);
    }

    @Override
    protected ImmutableList<ItemStack> getOutputsForRecipe(DoubleOutputRecipe recipe) {
        return ImmutableList.of(recipe.getOutput(), recipe.getOutput2());
    }

    @Override
    protected int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    protected int[] getInputSlots() {
        return INPUT_SLOT;
    }

    private boolean canOutput(ItemStack stack, int slot) {
        ItemStack existing = inventory.getStack(slot);
        return existing.isEmpty() || (existing.isItemEqual(stack) && ItemStack.areTagsEqual(existing, stack) && existing.getCount() + stack.getCount() <= stack.getMaxCount());
    }




}
