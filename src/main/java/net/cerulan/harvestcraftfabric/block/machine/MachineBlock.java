package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.MachineBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Supplier;

public class MachineBlock<T extends BlockEntity> extends BlockWithEntity implements InventoryProvider {

    private final Supplier<T> beSupplier;

    public MachineBlock(Supplier<T> beSupplier) {
        super(FabricBlockSettings.of(Material.METAL)
                .breakByTool(FabricToolTags.PICKAXES)
                .sounds(BlockSoundGroup.METAL)
                .strength(3f, 8f));
        this.beSupplier = beSupplier;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return beSupplier.get();
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null && MachineBlockEntity.isMachineBE(be)) {
            MachineBlockEntity<?> presser = (MachineBlockEntity<?>) be;
            return presser.getInventory(state, world, pos);
        } else return null;
    }
}
