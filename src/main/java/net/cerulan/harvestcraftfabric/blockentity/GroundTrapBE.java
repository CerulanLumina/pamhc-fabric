package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.gui.GroundTrapGuiDescription;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.Tag;

public class GroundTrapBE extends TrapBlockEntity {
    private static final Tag<Block> groundTrapSoils = TagRegistry.block(LocalPam.modID("ground_trap_soils"));
    public GroundTrapBE() {
        super("ground", TagRegistry.item(LocalPam.modID("bait/ground_bait")), ModBlockEntities.GROUND_TRAP_BE_TYPE, GroundTrapGuiDescription::new);
    }

    @Override
    protected boolean blockPredicate(BlockState state) {
        return groundTrapSoils.contains(state.getBlock());
    }
}
