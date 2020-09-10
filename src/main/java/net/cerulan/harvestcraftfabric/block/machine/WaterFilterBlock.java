package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.WaterFilterBlockEntity;

public class WaterFilterBlock extends FacingMachineBlock<WaterFilterBlockEntity> {

    public WaterFilterBlock() {
        super(WaterFilterBlockEntity::new);
    }

}
