package net.cerulan.harvestcraftfabric.rei.category;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.cerulan.harvestcraftfabric.block.machine.MachineRegistry;
import net.cerulan.harvestcraftfabric.rei.display.MarketDisplay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MarketCategory implements RecipeCategory<MarketDisplay> {
    private final Identifier id;
    public MarketCategory(Identifier id) {
        this.id = id;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(MachineRegistry.MARKET_BLOCK);
    }

    private static final int GRID_WIDTH = 4;
    private static final int GRID_HEIGHT = 4;
    private static final int GRID_COUNT = GRID_WIDTH * GRID_HEIGHT;
    private static final int SLOT_DELTA = 18;

    @Override
    public List<Widget> setupDisplay(MarketDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList(Widgets.createRecipeBase(bounds));

        int xBase = bounds.x + (bounds.width - SLOT_DELTA * GRID_WIDTH) * 3 / 4;
        int yBase = bounds.y + (bounds.height - MarketCategory.SLOT_DELTA * GRID_HEIGHT) / 2;

        List<EntryStack> buyable = recipeDisplay.getOutputEntries();
        Iterator<EntryStack> buyIter = buyable.iterator();
        List<List<EntryStack>> entries = Lists.newArrayListWithCapacity(GRID_COUNT);
        int eachSlot = buyable.size() / GRID_COUNT;
        Stream.generate(() -> Lists.<EntryStack>newArrayListWithCapacity(eachSlot))
                .peek(list -> IntStream.range(0, eachSlot).filter(i -> buyIter.hasNext()).mapToObj(i -> buyIter.next()).forEach(list::add))
                .limit(GRID_COUNT).forEach(entries::add);
        int remaining = 0;
        while (buyIter.hasNext()) {
            entries.get(remaining % GRID_COUNT).add(buyIter.next());
            ++remaining;
        }
        for (int i = 0; i < 16; ++i) {
            int xOff = i % GRID_WIDTH;
            int yOff = i / GRID_HEIGHT;
            widgets.add(Widgets.createSlot(new Point(xBase + xOff * SLOT_DELTA, yBase + yOff * MarketCategory.SLOT_DELTA)).entries(entries.get(i)).markOutput());
        }

        widgets.add(Widgets.createSlot(new Point((xBase - bounds.x) / 3 + bounds.x, yBase + SLOT_DELTA * (GRID_HEIGHT / 2 - (1 - (GRID_HEIGHT % 2))) + (GRID_HEIGHT % 2 == 0 ? SLOT_DELTA / 2 : 0)))
                .entries(recipeDisplay.getInputEntries().get(0))
                .markInput());

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
        return I18n.translate(MachineRegistry.MARKET_BLOCK.getTranslationKey());
    }
}
