package net.cerulan.harvestcraftfabric.block;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class PamFruitBlock extends PlantBlock implements Fertilizable {

    public static final Property<Integer> AGE_PROPERTY = IntProperty.of("age", 0, 2);
    public static final int MAX_AGE = 2;

    private static final VoxelShape OUTLINE = VoxelShapes.cuboid(0.2, 0.5, 0.2, 0.8, 1.0, 0.8);

    protected Identifier fruitItemID;

    public PamFruitBlock(Identifier fruitItemID) {
        super(FabricBlockSettings.copyOf(Blocks.COCOA).noCollision().breakInstantly().breakByHand(true).ticksRandomly());
        this.fruitItemID = fruitItemID;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE_PROPERTY);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return world.getBlockState(blockPos).isIn(TagRegistry.block(new Identifier("minecraft", "leaves")));
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.AIR && !this.collidable || super.canPathfindThrough(state, world, pos, type);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE_PROPERTY) == MAX_AGE) {
            player.incrementStat(Stats.MINED.getOrCreateStat(this));
            player.addExhaustion(0.005F);
            world.setBlockState(pos, getDefaultState(), 19);
            if (world.isClient()) return ActionResult.SUCCESS;
            player.inventory.offerOrDrop(world, new ItemStack(Registry.ITEM.get(fruitItemID)));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        player.incrementStat(Stats.MINED.getOrCreateStat(this));
        player.addExhaustion(0.005F);
        world.setBlockState(pos, getDefaultState(), 19);
        if (state.get(AGE_PROPERTY) == MAX_AGE) {
            player.inventory.offerOrDrop(world, new ItemStack(Registry.ITEM.get(fruitItemID)));
        }
    }

    public int getAge(BlockState state) {
        return state.get(AGE_PROPERTY);
    }

    public boolean notMature(BlockState state) {
        return getAge(state) < MAX_AGE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return this.notMature(state);
    }

    public BlockState withAge(int age) {
        return this.getDefaultState().with(AGE_PROPERTY, age);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < MAX_AGE) {
                if (random.nextInt(25) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }

    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int possibleGrowth = this.getAge(state) + this.getGrowthAmount(world);
        if (possibleGrowth > MAX_AGE) {
            possibleGrowth = MAX_AGE;
        }

        world.setBlockState(pos, this.withAge(possibleGrowth), 2);
    }

    protected int getGrowthAmount(World world) {
        return world.random.nextFloat() < 0.5f ? 1 : 0;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Registry.ITEM.get(this.fruitItemID));
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return this.notMature(state);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

}
