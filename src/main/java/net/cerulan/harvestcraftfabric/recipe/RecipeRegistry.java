package net.cerulan.harvestcraftfabric.recipe;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public class RecipeRegistry {

    public static RecipeType<DoubleOutputRecipe> PRESSER;
    public static RecipeSerializer<DoubleOutputRecipe> PRESSER_SERIALIZER;

    public static RecipeType<DoubleOutputRecipe> GRINDER;
    public static RecipeSerializer<DoubleOutputRecipe> GRINDER_SERIALIZER;

    public static final ObjectOpenHashSet<RecipeType<?>> RECIPE_TYPES = new ObjectOpenHashSet<>();

    public static void regiterRecipeHandlers() {

        register("presser", DoubleOutputRecipe.class, new DoubleOutputRecipe.Serializer<>(PresserRecipe::new), (type, serializer) -> {
            PRESSER = type;
            PRESSER_SERIALIZER = serializer;
        });
        register("grinder", DoubleOutputRecipe.class, new DoubleOutputRecipe.Serializer<>(GrinderRecipe::new), (type, serializer) -> {
            GRINDER = type;
            GRINDER_SERIALIZER = serializer;
        });

    }

    private static <T extends Recipe<?>> void register(String name, Class<T> type, RecipeSerializer<T> serializer, BiConsumer<RecipeType<T>, RecipeSerializer<T>> biConsumer) {
        TypeSerializer<T> registered = registerRecipeType(name, type, serializer);
        biConsumer.accept(registered.recipeType, registered.recipeSerializer);
    }

    private static <T extends Recipe<?>> TypeSerializer<T> registerRecipeType(String name, Class<T> type, RecipeSerializer<T> serializer) {
        RecipeSerializer<T> recipeSerializer = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("harvestcraft", name), serializer);
        RecipeType<T> recipeType = Registry.register(Registry.RECIPE_TYPE, new Identifier("harvestcraft", name), createRecipeType(name, type));
        RECIPE_TYPES.add(recipeType);
        return new TypeSerializer<>(recipeType, recipeSerializer);
    }

    private static <T extends Recipe<?>> RecipeType<T> createRecipeType(String name, Class<T> type) {
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return "harvestcraft:" + name;
            }
        };
    }

    private static class TypeSerializer<T extends Recipe<?>> {
        public RecipeType<T> recipeType;
        public RecipeSerializer<T> recipeSerializer;
        public TypeSerializer(RecipeType<T> recipeType, RecipeSerializer<T> recipeSerializer) {
            this.recipeSerializer = recipeSerializer;
            this.recipeType = recipeType;
        }
    }

}
