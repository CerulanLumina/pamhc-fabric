package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.WaterFilterGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class WaterFilterScreen extends CottonInventoryScreen<WaterFilterGuiDescription> {

    public WaterFilterScreen(WaterFilterGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }
}
