package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.MarketGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class MarketScreen extends CottonInventoryScreen<MarketGuiDescription> {

    public MarketScreen(MarketGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }

}
