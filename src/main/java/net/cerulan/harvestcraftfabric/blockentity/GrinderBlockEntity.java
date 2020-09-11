package net.cerulan.harvestcraftfabric.blockentity;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.gui.GrinderGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.item.ItemStack;

public class GrinderBlockEntity extends MachineBlockEntity<DoubleOutputRecipe> {

    public GrinderBlockEntity() {
        super(ModBlockEntities.GRINDER_BE_TYPE, RecipeRegistry.GRINDER, new MachineInventory(3, PresserBlockEntity.OUTPUT_SLOTS, PresserBlockEntity.INPUT_SLOT), GrinderGuiDescription::new);
    }

    @Override
    protected int getMaximumProgress() {
        return ConfigHandler.getGeneralConfig().machineConfig.grinderTicks;
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
        return PresserBlockEntity.OUTPUT_SLOTS;
    }

    @Override
    protected int[] getInputSlots() {
        return PresserBlockEntity.INPUT_SLOT;
    }

}
