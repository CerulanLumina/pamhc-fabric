package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.PresserBlockEntity;

public class PresserMachineBlock extends FacingMachineBlock<PresserBlockEntity> {
    public PresserMachineBlock() {
        super(PresserBlockEntity::new);
    }
}
