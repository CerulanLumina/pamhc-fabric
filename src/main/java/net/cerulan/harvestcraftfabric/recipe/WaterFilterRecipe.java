package net.cerulan.harvestcraftfabric.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WaterFilterRecipe extends DoubleOutputRecipe {
    public WaterFilterRecipe(Identifier identifier, Ingredient input, ItemStack output, ItemStack output2, String group) {
        super(identifier, input, output, output2, group);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.WATERFILTER_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.WATERFILTER;
    }

    @Override
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Registry.ITEM.get(new Identifier("harvestcraft:waterfilter")));
    }
}