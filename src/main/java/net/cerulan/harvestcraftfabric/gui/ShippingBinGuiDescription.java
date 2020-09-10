package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ShippingBinGuiDescription extends SyncedGuiDescription {
    public static final Identifier buyable = LocalPam.modID("buyable");
    private static final int INVENTORY_SIZE = 10;
    private final Tag<Item> buyableTag;
    public ShippingBinGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.SHIPPINGBIN_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        buyableTag = TagRegistry.item(buyable);
        WGridPanel root = new WGridPanel();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                WItemSlot inputSlot = WItemSlot.of(blockInventory, 1 + i * 3 + j);
                inputSlot.setFilter(stack -> buyableTag.contains(stack.getItem()));
                root.add(inputSlot, i, j);
            }
        }

        WItemSlot outputSlot = WItemSlot.outputOf(blockInventory, 0).setInsertingAllowed(false);
        root.add(outputSlot, 4, 1);

        root.add(createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
        setRootPanel(root);
    }

}
