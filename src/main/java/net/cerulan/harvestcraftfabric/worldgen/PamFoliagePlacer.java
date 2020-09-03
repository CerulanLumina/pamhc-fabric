package net.cerulan.harvestcraftfabric.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class PamFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<PamFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            method_28838(instance)
                    .and(
                            BlockState.CODEC.fieldOf("fruitBlockState").forGetter(PamFoliagePlacer::getFruitBlockState)
                    ).apply(instance, PamFoliagePlacer::new)
    );

    private final BlockState fruitBlockState;

    public PamFoliagePlacer(UniformIntDistribution radius, UniformIntDistribution offset, int height, BlockState fruitBlockState) {
        super(radius, offset, height);
        this.fruitBlockState = fruitBlockState;
    }

    @Override
    protected void generate(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int offset, BlockBox box) {
        super.generate(world, random, config, trunkHeight, treeNode, foliageHeight, radius, leaves, offset, box);
        leaves.stream().filter(uwu -> random.nextBoolean()).forEach(blockPos -> {
            world.setBlockState(blockPos, fruitBlockState, 19);
        });
        // Generate fruits
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return PamWorldGenerator.TREE_FOLIAGE_PLACER_TYPE;
    }

    public BlockState getFruitBlockState() {
        return fruitBlockState;
    }

}