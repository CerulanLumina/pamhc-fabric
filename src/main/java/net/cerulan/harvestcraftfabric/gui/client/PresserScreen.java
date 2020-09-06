package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PresserScreen extends CottonInventoryScreen<PresserGuiDescription> {
    public PresserScreen(PresserGuiDescription description, PlayerEntity playerEntity, Text title) {
        super(description, playerEntity, title);
    }
}
