package net.cerulan.harvestcraftfabric.rei.display;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.utils.CollectionUtils;
import net.cerulan.harvestcraftfabric.gui.MarketGuiDescription;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
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
        inputs = Collections.singletonList(EntryStack.ofItems(CollectionUtils.map(TagRegistry.item(MarketGuiDescription.buyable).values(), Item::asItem)));
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
