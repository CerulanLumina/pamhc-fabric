package net.cerulan.harvestcraftfabric.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.rei.display.DoubleOutputDisplay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class DoubleOutputCategory<R extends DoubleOutputRecipe> implements RecipeCategory<DoubleOutputDisplay<R>> {

    private final ItemConvertible icon;
    private final Identifier id;

    public DoubleOutputCategory(ItemConvertible icon, Identifier id) {
        this.icon = icon;
        this.id = id;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(icon);
    }

    @Override
    public String getCategoryName() {
        return I18n.translate(icon.asItem().getTranslationKey());
    }

    @Override
    public List<Widget> setupDisplay(DoubleOutputDisplay<R> recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 66, bounds.y + 18)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 48, bounds.y + 54)).entries(Collections.singletonList(recipeDisplay.getOutputEntries().get(0))).markOutput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 84, bounds.y + 54)).entries(Collections.singletonList(recipeDisplay.getOutputEntries().get(1))).markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 88;
    }

}
