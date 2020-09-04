package net.cerulan.harvestcraftfabric.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PamGardenBlock extends PlantBlock {

    private static final VoxelShape OUTLINE = VoxelShapes.cuboid(0.15, 0.0, 0.15, 0.85, 0.85, 0.85);
    private static final int MAX_SPREAD_XZ = 2;
    private static final int MAX_SPREAD_Y = 1;

    public PamGardenBlock() {
        super(FabricBlockSettings.copyOf(Blocks.PINK_TULIP).ticksRandomly());
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    private static final Identifier TAG_PLACEABLE = new Identifier("harvestcraft", "garden_growable");

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return TagRegistry.block(TAG_PLACEABLE).contains(floor.getBlock());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() <= 0.05f) {
            List<BlockPos> possibleSpots =
                    BlockPos.streamOutwards(pos, MAX_SPREAD_XZ, MAX_SPREAD_Y, MAX_SPREAD_XZ)
                            .map(BlockPos::toImmutable)
                            .filter(p -> world.getBlockState(p).isAir())
                            .filter(p -> canPlaceAt(state, world, p))
                            .collect(Collectors.toList());
            if (possibleSpots.isEmpty()) return;
            BlockPos selected = possibleSpots.get(random.nextInt(possibleSpots.size()));
            world.setBlockState(selected, state);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE;
    }
}
