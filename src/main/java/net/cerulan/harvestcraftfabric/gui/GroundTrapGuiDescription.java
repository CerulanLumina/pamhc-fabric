package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.cerulan.harvestcraftfabric.gui.client.MachineBackgroundPainter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class GroundTrapGuiDescription extends SyncedGuiDescription {

    private static final Identifier texture = new Identifier("harvestcraft", "textures/gui/groundtrap.png");
    private static final MachineBackgroundPainter painter = new MachineBackgroundPainter(texture)
            .setPadding(7, 7, 16, 17);

    public static final int INVENTORY_SIZE = 19;

    public GroundTrapGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.GROUNDTRAP_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        setupTrapGui(this, blockInventory);
    }

    static void setupTrapGui(SyncedGuiDescription description, Inventory blockInventory) {
        description.setTitleColor(0xe5e5e5ff);
        description.setTitleAlignment(HorizontalAlignment.LEFT);

        WGridPanel root = new WGridPanel();

        root.add(WItemSlot.of(blockInventory, 0), 1, 1);

        // across then down
        for (int j = 0; j < 3; ++j) { // loop down
            for (int i = 0; i < 6; ++i) { // loop across
                WItemSlot invSlot = WItemSlot.of(blockInventory,  1 + j * 6 + i).setInsertingAllowed(false);
                root.add(invSlot,  3 + i, j);
            }
        }
        WPlainPanel panel = new WPlainPanel();
        panel.add(description.createPlayerInventoryPanel(), 0, 0);
        root.add(panel, 0, 3);
        root.validate(description);
        description.setRootPanel(root);
    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(painter);
    }
}
