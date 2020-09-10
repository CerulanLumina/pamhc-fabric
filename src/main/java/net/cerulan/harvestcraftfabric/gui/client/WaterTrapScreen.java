package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.WaterTrapGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class WaterTrapScreen extends CottonInventoryScreen<WaterTrapGuiDescription> {
    public WaterTrapScreen(WaterTrapGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }

    @Override
    protected void reposition(int screenWidth, int screenHeight) {
        super.reposition(screenWidth, screenHeight);
        this.titleY = -16;
    }
}
