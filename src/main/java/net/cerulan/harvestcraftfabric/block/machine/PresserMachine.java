package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.blockentity.ModBlockEntities;
import net.cerulan.harvestcraftfabric.blockentity.PresserBlockEntity;
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

public class PresserMachine extends BlockWithEntity implements InventoryProvider {
    public PresserMachine() {
        super(FabricBlockSettings.of(Material.METAL)
                .breakByTool(FabricToolTags.PICKAXES)
                .sounds(BlockSoundGroup.METAL)
                .strength(3f, 8f));
        setDefaultState(getDefaultState().with(MachineRegistry.FACING_PROPERTY, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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
        return new PresserBlockEntity();
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null && be.getType() == ModBlockEntities.PRESSER_BE_TYPE) {
            PresserBlockEntity presser = (PresserBlockEntity)be;
            return presser.getInventory(state, world, pos);
        } else return null;
    }
}
