package net.cerulan.harvestcraftfabric.rei.display;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class TreeDisplay implements RecipeDisplay {
    private final Identifier category;
    private final List<List<EntryStack>> inputs;
    private final List<EntryStack> outputs;
    public TreeDisplay(Identifier category, ItemConvertible sapling, ItemConvertible fruit) {
        this.category = category;
        this.inputs = Collections.singletonList(Collections.singletonList(EntryStack.create(sapling)));
        this.outputs = Collections.singletonList(EntryStack.create(fruit));
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
        return category;
    }
}
