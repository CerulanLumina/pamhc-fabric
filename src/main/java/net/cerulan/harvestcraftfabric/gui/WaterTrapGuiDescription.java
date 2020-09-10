package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.cerulan.harvestcraftfabric.gui.client.MachineBackgroundPainter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class WaterTrapGuiDescription extends SyncedGuiDescription {

    private static final Identifier texture = new Identifier("harvestcraft", "textures/gui/groundtrap.png");
    private static final MachineBackgroundPainter painter = new MachineBackgroundPainter(texture)
            .setPadding(6, 8, 8, 17);

    public static final int INVENTORY_SIZE = 19;

    public WaterTrapGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.GROUNDTRAP_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
        GroundTrapGuiDescription.setupTrapGui(this, blockInventory);
    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(painter);
    }
}
