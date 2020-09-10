package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.GroundTrapGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class GroundTrapScreen extends CottonInventoryScreen<GroundTrapGuiDescription> {
    public GroundTrapScreen(GroundTrapGuiDescription description, PlayerEntity player, Text text) {
        super(description, player, text);
    }

    @Override
    protected void reposition(int screenWidth, int screenHeight) {
        super.reposition(screenWidth, screenHeight);
        this.titleY = -16;
    }
}
