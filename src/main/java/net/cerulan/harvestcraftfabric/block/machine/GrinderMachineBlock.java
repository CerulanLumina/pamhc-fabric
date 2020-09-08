package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.GrinderBlockEntity;

public class GrinderMachineBlock extends FacingMachineBlock<GrinderBlockEntity> {

    public GrinderMachineBlock() {
        super(GrinderBlockEntity::new);
    }

}
