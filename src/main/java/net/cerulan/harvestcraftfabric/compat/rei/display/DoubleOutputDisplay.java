package net.cerulan.harvestcraftfabric.compat.rei.display;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DoubleOutputDisplay<R extends DoubleOutputRecipe> implements RecipeDisplay {
    private final R recipe;
    private final List<List<EntryStack>> inputs;
    private final List<EntryStack> outputs;
    private final Identifier categoryId;
    public DoubleOutputDisplay(R recipe, Identifier categoryId) {
        this.recipe = recipe;
        this.categoryId = categoryId;
        this.inputs = Collections.singletonList(EntryStack.ofIngredient(recipe.getInput()));
        this.outputs = EntryStack.ofItemStacks(Lists.newArrayList(recipe.getOutput(), recipe.getOutput2()));
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
    public List<List<EntryStack>> getRequiredEntries() {
        return inputs;
    }

    @Override
    public Identifier getRecipeCategory() {
        return categoryId;
    }

    @Override
    public Optional<Identifier> getRecipeLocation() {
        return Optional.of(recipe.getId());
    }
}
