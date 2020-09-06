package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.cerulan.harvestcraftfabric.gui.client.FGBar;
import net.cerulan.harvestcraftfabric.gui.client.MachineBackgroundPainter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class PresserGuiDescription extends SyncedGuiDescription {
    private static final int INVENTORY_SIZE = 3;
    private static final Identifier barTexture = new Identifier("harvestcraft", "textures/gui/presser.png");

    private static final MachineBackgroundPainter painter = new MachineBackgroundPainter(
            new Identifier("harvestcraft", "textures/gui/presser.png")
    ).setPadding(6, 8, 0, 17);

    public PresserGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.PRESSER_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context, 2));

        WGridPanel root = new WGridPanel();

        setRootPanel(root);

        WPlainPanel panel = new WPlainPanel();
        root.add(panel, 0, 0);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        WItemSlot itemSlot2 = WItemSlot.of(blockInventory, 1);
        WItemSlot itemSlot3 = WItemSlot.of(blockInventory, 2);
        panel.add(itemSlot, 72, 22);
        panel.add(itemSlot2, 54, 54);
        panel.add(itemSlot3, 90, 54);

        FGBar progressBar = new FGBar(null, barTexture, 0, 1, WBar.Direction.RIGHT, new Vec2f(0.6875f, 0.0703125f), new Vec2f(0.78125f, 0.12109375f));
        panel.add(progressBar, 70, 8);


        this.setTitleColor(0xe5e5e5ff);
        root.add(this.createPlayerInventoryPanel(), 0, 4);
        root.validate(this);
        progressBar.setSize(24, 13);
    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(painter);
    }
}