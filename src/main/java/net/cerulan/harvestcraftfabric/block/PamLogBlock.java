package net.cerulan.harvestcraftfabric.block;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PamLogBlock extends PillarBlock implements Fertilizable {

    private static final int MATURE_AGE = 2;

    public static final IntProperty AGE = IntProperty.of("age", 0, MATURE_AGE);

    private final Identifier treeLoot;
    public PamLogBlock(String tree) {
        super(FabricBlockSettings.copyOf(Blocks.OAK_LOG));
        this.treeLoot = new Identifier("harvestcraft", "trees/" + tree);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) == MATURE_AGE) {
            if (world.isClient) return ActionResult.SUCCESS;
            getLoot((ServerWorld)world).forEach(stack -> player.inventory.offerOrDrop(world, stack));
            world.setBlockState(pos, state.with(AGE, 0));
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;
    }

    private List<ItemStack> getLoot(ServerWorld world) {
        List<ItemStack> list = Lists.newArrayList();
        Objects.requireNonNull(world.getServer())
                .getLootManager()
                .getTable(treeLoot)
                .generateUnprocessedLoot(new LootContext.Builder(world).build(LootContextTypes.EMPTY), list::add);
        return list;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < MATURE_AGE;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int possibleGrowth = state.get(AGE) + this.getGrowthAmount(world);
        if (possibleGrowth > MATURE_AGE) {
            possibleGrowth = MATURE_AGE;
        }

        world.setBlockState(pos, this.getDefaultState().with(AGE, possibleGrowth), 2);
    }

    protected int getGrowthAmount(World world) {
        return world.random.nextFloat() < 0.5f ? 1 : 0;
    }
}
