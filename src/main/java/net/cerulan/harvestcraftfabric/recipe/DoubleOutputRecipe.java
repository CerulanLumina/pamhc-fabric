package net.cerulan.harvestcraftfabric.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public abstract class DoubleOutputRecipe implements Recipe<Inventory> {

    protected final Identifier identifier;
    protected final ItemStack output, output2;
    protected final Ingredient input;
    protected final String group;

    public DoubleOutputRecipe(Identifier identifier, Ingredient input, ItemStack output, ItemStack output2, String group) {
        this.identifier = identifier;
        this.input = input;
        this.output = output;
        this.output2 = output2;
        this.group = group;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return input.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    public ItemStack getOutput2() {
        return output2.copy();
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public static final class Serializer<T extends DoubleOutputRecipe> implements RecipeSerializer<DoubleOutputRecipe> {

        private final DoubleOutputSerializerSupplier supplier;

        public Serializer(DoubleOutputSerializerSupplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public DoubleOutputRecipe read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "output_one"));
            ItemStack output2 = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "output_two"));
            String group = JsonHelper.getString(json, "group", "");
            return supplier.supply(id, input, output, output2, group);
        }

        @Override
        public DoubleOutputRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack output = buf.readItemStack();
            ItemStack output2 = buf.readItemStack();
            String group = buf.readString();
            Ingredient input = Ingredient.fromPacket(buf);
            return supplier.supply(id, input, output, output2, group);
        }

        @Override
        public void write(PacketByteBuf buf, DoubleOutputRecipe recipe) {
            buf.writeItemStack(recipe.output);
            buf.writeItemStack(recipe.output2);
            buf.writeString(recipe.group);
            recipe.input.write(buf);;
        }
    }

    @FunctionalInterface
    public interface DoubleOutputSerializerSupplier {
        DoubleOutputRecipe supply(Identifier id, Ingredient input, ItemStack output, ItemStack output2, String group);
    }
}
