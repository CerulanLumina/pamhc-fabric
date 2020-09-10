package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.TrapBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.function.Supplier;

public class TrapBlock extends BlockWithEntity implements InventoryProvider {


    private final Supplier<TrapBlockEntity> supplier;
    public TrapBlock(Supplier<TrapBlockEntity> supplier) {

        super(FabricBlockSettings.of(Material.WOOD)
                .breakByTool(FabricToolTags.AXES)
                .breakByHand(true)
                .strength(2f, 3f)
                .sounds(BlockSoundGroup.WOOD));
        this.supplier = supplier;
        setDefaultState(getDefaultState().with(MachineRegistry.FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MachineRegistry.FACING_PROPERTY);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(MachineRegistry.FACING_PROPERTY, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return supplier.get();
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof TrapBlockEntity) {
            TrapBlockEntity trapBlockEntity = (TrapBlockEntity) be;
            return trapBlockEntity.getInventory();
        } else return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
