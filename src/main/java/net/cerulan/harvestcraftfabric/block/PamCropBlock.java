package net.cerulan.harvestcraftfabric.block;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class PamCropBlock extends CropBlock {

    public static final IntProperty AGE;
    private static final VoxelShape[] AGE_TO_SHAPE;

    private ItemConvertible seedsItem;

    public PamCropBlock() {
        super(FabricBlockSettings.copyOf(Blocks.CARROTS).nonOpaque());

    }

    public void setSeedsItem(ItemConvertible seedsItem) {
        this.seedsItem = seedsItem;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        ItemTags.getRequiredTags().forEach(a -> {
            Harvestcraftfabric.LOGGER.info(((Tag.Identified<Item>)a).getId() + " :: " + ((Tag.Identified<Item>)a).values().toString());
        });
//        Tag<Item> item = TagRegistry.item(new Identifier("harvestcraft:milk"));
//        Harvestcraftfabric.LOGGER.info(item.values());
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected ItemConvertible getSeedsItem() {
        return this.seedsItem;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(3) != 0) {
            super.scheduledTick(state, world, pos, random);
        }
    }

    @Override
    protected int getGrowthAmount(World world) {
        return super.getGrowthAmount(world) / 3;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
    }

    static {
        AGE = IntProperty.of("age", 0, 3);
        AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D)};
    }
}