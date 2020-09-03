package net.cerulan.harvestcraftfabric.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Rip-off of https://github.com/TechReborn/TechReborn/blob/1.16/src/main/java/techreborn/world/RubberTreeDecorator.java
public class BiomeFruitTreeDecorator extends Decorator<ChanceDecoratorConfig> {
    public BiomeFruitTreeDecorator(Codec<ChanceDecoratorConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, ChanceDecoratorConfig config, BlockPos pos) {
        if (random.nextInt(config.chance) == 0) {
            // generate 2 - 4 trees
            int treeCount = 2 + random.nextInt(3);
            return IntStream.range(0, treeCount).mapToObj(i -> {
                int x = random.nextInt(16) + pos.getX();
                int z = random.nextInt(16) + pos.getZ();
                int y = context.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
                return new BlockPos(x, y, z);
            });
        }
        return Stream.empty();
    }
}
