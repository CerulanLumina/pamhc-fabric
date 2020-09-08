package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.block.machine.MachineRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static BlockEntityType<PresserBlockEntity> PRESSER_BE_TYPE;
    public static BlockEntityType<GrinderBlockEntity> GRINDER_BE_TYPE;

    public static void registerBlockEntities() {
        PRESSER_BE_TYPE = registerBlockEntity("presser", PresserBlockEntity::new, MachineRegistry.PRESSER_MACHINE);
        GRINDER_BE_TYPE = registerBlockEntity("grinder", GrinderBlockEntity::new, MachineRegistry.GRINDER_MACHINE);
    }

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String localId, Supplier<T> beSupplier, Block block) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier("harvestcraft", localId),
                BlockEntityType.Builder.create(beSupplier, block).build(null));
    }

}
