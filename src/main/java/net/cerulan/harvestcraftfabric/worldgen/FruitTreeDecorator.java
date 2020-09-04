package net.cerulan.harvestcraftfabric.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.decorator.TreeDecorator;
import net.minecraft.world.gen.decorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class FruitTreeDecorator extends TreeDecorator {

    public static final Codec<FruitTreeDecorator> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                BlockState.CODEC
                        .fieldOf("fruitBlockState")
                        .forGetter(FruitTreeDecorator::getFruitBlockState))
//                .and(Codec
//                        .floatRange(0.0f, 1.0f)
//                        .fieldOf("fruitProbability")
//                        .forGetter(FruitTreeDecorator::getFruitProbability))
                .apply(instance, FruitTreeDecorator::new)
    );

    float fruitProbability;
    BlockState fruitBlockState;
    public FruitTreeDecorator(BlockState fruitBlockState) {
        this.fruitBlockState = fruitBlockState;
        this.fruitProbability = 0.25f;
    }

    public BlockState getFruitBlockState() {
        return fruitBlockState;
    }

    public float getFruitProbability() {
        return fruitProbability;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return PamWorldGenerator.TREE_DECORATOR_TYPE;
    }

    @Override
    public void generate(WorldAccess world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> set, BlockBox box) {
        leavesPositions.stream().filter(pos -> world.getBlockState(pos.down()).isAir()).filter(p -> random.nextFloat() < fruitProbability).forEach(pos -> world.setBlockState(pos.down(), fruitBlockState, 19));
    }
}
