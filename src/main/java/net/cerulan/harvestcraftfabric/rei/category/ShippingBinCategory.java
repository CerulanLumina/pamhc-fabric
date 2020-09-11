package net.cerulan.harvestcraftfabric.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Slot;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.cerulan.harvestcraftfabric.block.machine.MachineRegistry;
import net.cerulan.harvestcraftfabric.rei.display.ShippingBinDisplay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.List;

public class ShippingBinCategory implements RecipeCategory<ShippingBinDisplay> {
    private final Identifier id;
    public ShippingBinCategory(Identifier id) {
        this.id = id;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(MachineRegistry.SHIPPING_BIN_BLOCK);
    }

    private static final int GRID_WIDTH = 4;
    private static final int GRID_HEIGHT = 4;
    private static final int GRID_COUNT = GRID_WIDTH * GRID_HEIGHT;
    private static final int SLOT_DELTA = 18;

    @Override
    public List<Widget> setupDisplay(ShippingBinDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList(Widgets.createRecipeBase(bounds));

        int xBase = bounds.x + (bounds.width - SLOT_DELTA * GRID_WIDTH) / 4;
        int yBase = bounds.y + (bounds.height - SLOT_DELTA * GRID_HEIGHT) / 2;

        List<EntryStack> sellable = recipeDisplay.getInputEntries().get(0);
        MarketCategory.renderMorphingGrid(widgets, sellable, new Point(xBase, yBase), Slot::markInput, GRID_WIDTH, GRID_HEIGHT);

        widgets.add(Widgets.createSlot(new Point(xBase + SLOT_DELTA * (GRID_WIDTH + 1), yBase + SLOT_DELTA * (GRID_HEIGHT / 2 - (1 - (GRID_HEIGHT % 2))) + (GRID_HEIGHT % 2 == 0 ? SLOT_DELTA / 2 : 0)))
                .entries(recipeDisplay.getOutputEntries())
                .markOutput());

        return widgets;
    }


    @Override
    public int getDisplayHeight() {
        return 100;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public String getCategoryName() {
        return I18n.translate(MachineRegistry.SHIPPING_BIN_BLOCK.getTranslationKey());
    }

}
