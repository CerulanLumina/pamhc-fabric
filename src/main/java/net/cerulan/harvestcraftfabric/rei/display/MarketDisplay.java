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

public class MarketDisplay implements RecipeDisplay {

    private final List<List<EntryStack>> inputs;
    private final List<EntryStack> outputs;
    private final Identifier recipeCategory;
    public MarketDisplay(Identifier recipeCategory) {
        this.recipeCategory = recipeCategory;
        inputs = Collections.singletonList(Collections.singletonList(EntryStack.create(Items.EMERALD)));
        outputs = EntryStack.ofItems(CollectionUtils.map(TagRegistry.item(MarketGuiDescription.buyable).values(), Item::asItem));
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
