package net.cerulan.harvestcraftfabric;

import com.google.common.collect.Lists;
import net.cerulan.harvestcraftfabric.block.PamFruitBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HarvestcraftContent {

    private static final List<FruitTree> fruitTrees = Lists.newArrayList();

    private static abstract class Tree {
        protected final Item sapling;
        protected final Supplier<ItemStack> result;
        public Tree(ItemConvertible sapling, Supplier<ItemStack> result) {
            this.sapling = sapling.asItem();
            this.result = result;
        }

        public Supplier<ItemStack> getResult() {
            return result;
        }

        public Item getSapling() {
            return sapling;
        }
    }

    public static class FruitTree extends Tree {
        private final PamFruitBlock fruitBlock;
        public FruitTree(ItemConvertible sapling, Supplier<ItemStack> fruitResult, PamFruitBlock fruitBlock) {
            super(sapling, fruitResult);
            this.fruitBlock = fruitBlock;
        }

        public PamFruitBlock getFruitBlock() {
            return fruitBlock;
        }
    }

    public static void registerFruitTree(FruitTree tree) {
        fruitTrees.add(tree);
    }

    public static void forEachFruitTree(Consumer<FruitTree> consumer) {
        fruitTrees.forEach(consumer);
    }

    public static List<FruitTree> getFruitTrees() {
        return Collections.unmodifiableList(fruitTrees);
    }

}
