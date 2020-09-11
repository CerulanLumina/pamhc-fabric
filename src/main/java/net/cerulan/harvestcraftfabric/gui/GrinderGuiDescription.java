package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.cerulan.harvestcraftfabric.gui.client.MachineBackgroundPainter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class GrinderGuiDescription extends SyncedGuiDescription {
    private static final Identifier barTexture = new Identifier("harvestcraft", "textures/gui/grinder.png");
    private static final MachineBackgroundPainter painter = new MachineBackgroundPainter(barTexture)
            .setPadding(6, 8, 0, 17);

    public GrinderGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(GuiRegistry.GRINDER_HANDLER_TYPE, syncId, playerInventory, getBlockInventory(context, PresserGuiDescription.INVENTORY_SIZE), getBlockPropertyDelegate(context, 2));
        PresserGuiDescription.doubleOutputSetup(this, blockInventory, barTexture);
    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(painter);
    }
}
