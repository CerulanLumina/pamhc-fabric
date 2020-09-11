package net.cerulan.harvestcraftfabric.blockentity;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.item.ItemStack;

public class PresserBlockEntity extends MachineBlockEntity<DoubleOutputRecipe> {

    /*
    Down - output
    Other - input
     */
    static final int[] OUTPUT_SLOTS = new int[] {1, 2};
    static final int[] INPUT_SLOT = new int[] {0};

    public PresserBlockEntity() {
        super(ModBlockEntities.PRESSER_BE_TYPE, RecipeRegistry.PRESSER, new MachineInventory(3, OUTPUT_SLOTS, INPUT_SLOT), PresserGuiDescription::new);
    }

    @Override
    protected int getMaximumProgress() {
        return ConfigHandler.getGeneralConfig().machineConfig.presserTicks;
    }

    @Override
    protected boolean canOutput(DoubleOutputRecipe recipe) {
        return canPlaceStackInSlot(inventory, recipe.getOutput(), 1) && canPlaceStackInSlot(inventory, recipe.getOutput2(), 2);
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

}
