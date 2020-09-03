package net.cerulan.harvestcraftfabric.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PamWorldGenConfig {
    public static final Codec<PamWorldGenConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ConfiguredFeature.field_25833.fieldOf("fruitTree").forGetter(PamWorldGenConfig::getFruitTree)
            )
            .apply(instance, PamWorldGenConfig::new)
            );

    private final ConfiguredFeature<?, ?> fruitTree;

    public PamWorldGenConfig(ConfiguredFeature<?, ?> fruitTree) {
        this.fruitTree = fruitTree;
    }

    public ConfiguredFeature<?, ?> getFruitTree() {
        return fruitTree;
    }
}
