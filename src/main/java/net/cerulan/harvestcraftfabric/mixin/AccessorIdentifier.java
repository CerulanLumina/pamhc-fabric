package net.cerulan.harvestcraftfabric.mixin;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Identifier.class)
public interface AccessorIdentifier {
    @Invoker("split")
    static String[] split(String id, char delimiter) { throw new UnsupportedOperationException(); }

    @Invoker("isPathValid")
    static boolean isPathValid(String path) { throw new UnsupportedOperationException(); }

    @Invoker("isNamespaceValid")
    static boolean isNamespaceValid(String path) { throw new UnsupportedOperationException(); }
}
