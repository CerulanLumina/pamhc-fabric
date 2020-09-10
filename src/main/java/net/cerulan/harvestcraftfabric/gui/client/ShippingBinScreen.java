package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.cerulan.harvestcraftfabric.gui.ShippingBinGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ShippingBinScreen extends CottonInventoryScreen<ShippingBinGuiDescription> {

    public ShippingBinScreen(ShippingBinGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }
}
