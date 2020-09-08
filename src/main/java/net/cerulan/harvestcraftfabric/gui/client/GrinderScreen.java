package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.GrinderGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class GrinderScreen extends CottonInventoryScreen<GrinderGuiDescription> {
    public GrinderScreen(GrinderGuiDescription description, PlayerEntity playerEntity, Text title) {
        super(description, playerEntity, title);
    }
}
