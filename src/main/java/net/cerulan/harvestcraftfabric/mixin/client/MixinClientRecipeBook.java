package net.cerulan.harvestcraftfabric.mixin.client;

import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public abstract class MixinClientRecipeBook {

    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        RecipeType<?> type = recipe.getType();
        if (RecipeRegistry.RECIPE_TYPES.contains(type)) {
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }

}
