package net.cerulan.harvestcraftfabric.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.tree.TreeDecorator;
import net.minecraft.world.gen.tree.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeDecoratorType.class)
public interface AccessorTreeDecoratorType {
    @Invoker("register")
    static <P extends TreeDecorator> TreeDecoratorType<P> register(String id, Codec<P> codec) {
        throw new UnsupportedOperationException();
    }
}
