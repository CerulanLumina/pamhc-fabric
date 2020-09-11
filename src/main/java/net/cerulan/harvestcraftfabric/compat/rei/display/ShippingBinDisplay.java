package net.cerulan.harvestcraftfabric.compat.rei.display;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.cerulan.harvestcraftfabric.gui.ShippingBinGuiDescription;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class ShippingBinDisplay implements RecipeDisplay {
    private final List<List<EntryStack>> inputs;
    private final List<EntryStack> outputs;
    private final Identifier recipeCategory;
    public ShippingBinDisplay(Identifier recipeCategory) {
        this.recipeCategory = recipeCategory;
        inputs = Collections.singletonList(EntryStack.ofItemStacks(CollectionUtils.map(TagRegistry.item(ShippingBinGuiDescription.sellable).values(), item -> {
            ItemStack stack = new ItemStack(item.asItem());
            stack.setCount(32);
            return stack;
        })));
        outputs = Collections.singletonList(EntryStack.create(Items.EMERALD));
    }

    @Override
    public List<List<EntryStack>> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryStack> getOutputEntries() {
        return outputs;
    }

    @Override
    public Identifier getRecipeCategory() {
        return recipeCategory;
    }
}
