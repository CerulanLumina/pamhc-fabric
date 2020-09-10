package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.netty.buffer.Unpooled;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MarketGuiDescription extends SyncedGuiDescription {

    public static final Identifier buyable = LocalPam.modID("buyable");
    private static final int INVENTORY_SIZE = 2;

    BlockPos bePos = null;
    private final WFadedItem displayItem;

    public MarketGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, PacketByteBuf buf) {
        this(syncId, playerInventory, context);
        bePos = buf.readBlockPos();
    }

    public MarketGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.MARKET_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        WTextField filterField = new WTextField();
        root.add(filterField, 0, 1, 5, 1);

        List<ItemStack> stacks = TagRegistry.item(buyable).values().stream().map(ItemStack::new).collect(Collectors.toList());
        FilterableItemSelector itemPanel = new FilterableItemSelector(stacks, filterField::getText, 7);

        WScrollingPanel scrollPanel = new WScrollingPanel(itemPanel);
        root.add(scrollPanel, 0, 2, 9, 4);
        scrollPanel.setLocation(scrollPanel.getX(), scrollPanel.getY() + 4);
        scrollPanel.setSize(scrollPanel.getWidth(), scrollPanel.getHeight() + 6);
        root.add(createPlayerInventoryPanel(), 0, 7);
        itemPanel.setScrollingPanel(scrollPanel);

        WItemSlot input = WItemSlot.of(blockInventory, 0);
        WItemSlot output = WItemSlot.of(blockInventory, 1).setInsertingAllowed(false);
        WFadedItem emeraldItem = new WFadedItem(new ItemStack(Items.EMERALD));
        displayItem = new WFadedItem(ItemStack.EMPTY);

        root.add(input, 6, 1);
        root.add(emeraldItem, 6, 1);
        root.add(output, 8, 1);
        root.add(displayItem, 8, 1);

        itemPanel.setOnStackSelect(this::dispatchStackSelection);
        root.validate(this);
    }

    private void dispatchStackSelection(ItemStack stack) {
        displayItem.setItems(Collections.singletonList(stack));
        if (bePos != null) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBlockPos(bePos);
            buf.writeItemStack(stack);
            ClientSidePacketRegistry.INSTANCE.sendToServer(Harvestcraftfabric.Packets.SET_MARKET_STACK, buf);
        }
    }

}
