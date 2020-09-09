package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Language;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FilterableItemSelector extends WPanel {

    private final List<ItemButton> allItems;
    private final List<String> translationKeys;
    private final Supplier<String> filter;
    private String lastFilter;
    private final int gridWidth;
    private WScrollingPanel scrollingPanel = null;
    private Consumer<ItemStack> stackConsumer = null;

    public FilterableItemSelector(List<ItemStack> stacks, Supplier<String> filter, int gridWidth) {
        this.gridWidth = gridWidth;
        this.allItems = stacks.stream().map(ItemButton::new).collect(Collectors.toList());
        allItems.forEach(wButton -> {
            wButton.setParent(this);
            wButton.setSize(20, 20);
            wButton.setOnClick(() -> dispatchItemStack(wButton.getStack()));
        });
        this.translationKeys = stacks.stream().map(ItemStack::getItem).map(Item::getTranslationKey).collect(Collectors.toList());
        this.filter = filter;
        String nFilter = this.filter.get();
        handleButtons(nFilter);
        lastFilter = nFilter;
    }

    private void dispatchItemStack(ItemStack stack) {
        if (stackConsumer != null) stackConsumer.accept(stack);
    }

    public void setOnStackSelect(Consumer<ItemStack> stackConsumer) {
        this.stackConsumer = stackConsumer;
    }

    @Override
    public void tick() {
        allItems.forEach(WWidget::tick);
        String nFilter = filter.get();
        if (!lastFilter.equals(nFilter)) {
            handleButtons(nFilter);
            lastFilter = nFilter;
        }
    }

    private void handleButtons(String filter) {
        children.clear();
        int i = 0;
        int cx = 0, cy = 0;
        for (WButton button : allItems) {
            String key = translationKeys.get(i++);
            if (Language.getInstance().hasTranslation(key) && Language.getInstance().get(key).toLowerCase().contains(filter)) {
                button.setLocation(cx * 20, cy * 20);
                children.add(button);
                if (++cx == gridWidth){
                    cx = 0;
                    ++cy;
                }
            }
        }
    }

    @Override
    public void onMouseScroll(int x, int y, double amount) {
        if (scrollingPanel != null) scrollingPanel.onMouseScroll(x, y, amount);
    }

    public void setScrollingPanel(WScrollingPanel scrollingPanel) {
        this.scrollingPanel = scrollingPanel;
    }
}
