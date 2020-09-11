package net.cerulan.harvestcraftfabric.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.cerulan.harvestcraftfabric.rei.display.TreeDisplay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class TreeCategory implements RecipeCategory<TreeDisplay> {

    private final Identifier id;
    private final Supplier<EntryStack> logoSupplier;
    public TreeCategory(Identifier id, Supplier<EntryStack> logoSupplier) {
        this.id = id;
        this.logoSupplier = logoSupplier;
    }

    @Override
    public EntryStack getLogo() {
        return logoSupplier.get();
    }

    @Override
    public List<Widget> setupDisplay(TreeDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(bounds.x + 48, bounds.y + 24))
                .entries(recipeDisplay.getInputEntries().get(0))
                .markInput());

        widgets.add(Widgets.createSlot(new Point(bounds.x + 84, bounds.y + 24))
                .entries(recipeDisplay.getOutputEntries())
                .markOutput());

        return widgets;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public String getCategoryName() {
        return I18n.translate(String.format("rei.category.%s.%s", id.getNamespace(), id.getPath()));
    }
}
