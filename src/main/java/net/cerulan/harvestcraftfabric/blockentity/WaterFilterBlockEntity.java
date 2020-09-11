package net.cerulan.harvestcraftfabric.blockentity;

import com.google.common.collect.ImmutableList;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.gui.WaterFilterGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

public class WaterFilterBlockEntity extends MachineBlockEntity<DoubleOutputRecipe> {

    private boolean isInWater = false;
    private int waterCheckTimer = 200;
    private static final int MAX_WATER_CHECK_TIMER = 200;

    public WaterFilterBlockEntity() {
        super(ModBlockEntities.WATERFILTER_BE_TYPE, RecipeRegistry.WATERFILTER, new MachineInventory(3, PresserBlockEntity.OUTPUT_SLOTS, PresserBlockEntity.INPUT_SLOT), WaterFilterGuiDescription::new);
    }

    @Override
    public void tick() {
        if (waterCheckTimer == MAX_WATER_CHECK_TIMER) {
            checkWater();
            waterCheckTimer = 0;
        } else ++waterCheckTimer;
        if (isInWater)
            super.tick(); // process recipes
        else ticksProgress = 0;
    }

    private void checkWater() {
        assert world != null;
        isInWater = BlockPos.stream(pos.north(2).west(2), pos.south(2).east(2))
                .map(BlockPos::toImmutable)
                .filter(p -> !p.equals(pos))
                .filter(world::canSetBlock)
                .map(world::getFluidState)
                .map(FluidState::getFluid)
                .allMatch(state -> state.isIn(FluidTags.WATER));
    }

    @Override
    protected int getMaximumProgress() {
        return ConfigHandler.getGeneralConfig().machineConfig.waterFilterTicks;
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
