package net.cerulan.harvestcraftfabric.rei;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.cerulan.harvestcraftfabric.block.machine.MachineRegistry;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.cerulan.harvestcraftfabric.rei.category.DoubleOutputCategory;
import net.cerulan.harvestcraftfabric.rei.display.DoubleOutputDisplay;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

import java.util.function.Function;
import java.util.function.Predicate;

public class HarvestcraftPlugin implements REIPluginV0 {
    private static final Identifier pluginID = new Identifier("harvestcraft", "recipe_plugin");
    @Override
    public Identifier getPluginIdentifier() {
        return pluginID;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategories(new DoubleOutputCategory<>(MachineRegistry.PRESSER_MACHINE, new Identifier(RecipeRegistry.PRESSER.toString())));
        recipeHelper.registerCategories(new DoubleOutputCategory<>(MachineRegistry.GRINDER_MACHINE, new Identifier(RecipeRegistry.GRINDER.toString())));
        recipeHelper.registerCategories(new DoubleOutputCategory<>(MachineRegistry.WATER_FILTER_BLOCK, new Identifier(RecipeRegistry.WATERFILTER.toString())));
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        registerDoubleOutputRecipe(new Identifier(RecipeRegistry.PRESSER.toString()), recipeHelper, RecipeRegistry.PRESSER);
        registerDoubleOutputRecipe(new Identifier(RecipeRegistry.GRINDER.toString()), recipeHelper, RecipeRegistry.GRINDER);
        registerDoubleOutputRecipe(new Identifier(RecipeRegistry.WATERFILTER.toString()), recipeHelper, RecipeRegistry.WATERFILTER);
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(new Identifier(RecipeRegistry.PRESSER.toString()), EntryStack.create(MachineRegistry.PRESSER_MACHINE));
        recipeHelper.registerWorkingStations(new Identifier(RecipeRegistry.GRINDER.toString()), EntryStack.create(MachineRegistry.GRINDER_MACHINE));
        recipeHelper.registerWorkingStations(new Identifier(RecipeRegistry.WATERFILTER.toString()), EntryStack.create(MachineRegistry.WATER_FILTER_BLOCK));
    }

    private void registerDoubleOutputRecipe(Identifier categoryId, RecipeHelper recipeHelper, RecipeType<?> recipeType) {
        Function<DoubleOutputRecipe, RecipeDisplay> recipeDisplay = r -> new DoubleOutputDisplay<>(r, categoryId);
        recipeHelper.registerRecipes(categoryId, (Predicate<Recipe>)recipe -> recipe.getType() == recipeType, recipeDisplay);
    }
}
