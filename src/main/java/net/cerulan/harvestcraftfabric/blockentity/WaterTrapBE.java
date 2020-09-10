package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.gui.WaterTrapGuiDescription;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.tag.FluidTags;

public class WaterTrapBE extends TrapBlockEntity {
    public WaterTrapBE() {
        super("water", TagRegistry.item(LocalPam.modID("bait/water_bait")), ModBlockEntities.WATER_TRAP_BE_TYPE, WaterTrapGuiDescription::new);
    }

    @Override
    protected boolean blockPredicate(BlockState state) {
        return state.getFluidState().isIn(FluidTags.WATER);
    }
}
