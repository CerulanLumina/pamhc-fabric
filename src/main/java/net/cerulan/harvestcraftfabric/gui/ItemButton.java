package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringRenderable;

import java.util.List;

public class ItemButton extends WButton {

    private final ItemStack stack;

    public ItemButton(ItemStack stack) {
        setIcon(new ItemIcon(stack));
        this.stack = stack;
    }

    @Override
    public void addTooltip(List<StringRenderable> tooltip) {
        tooltip.addAll(stack.getTooltip(null, TooltipContext.Default.NORMAL));
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void onMouseScroll(int x, int y, double amount) {
        if (parent != null)
            parent.onMouseScroll(x, y, amount);
    }
}
