package net.cerulan.harvestcraftfabric.client;

import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.gui.GuiRegistry;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.cerulan.harvestcraftfabric.gui.client.PresserScreen;
import net.cerulan.harvestcraftfabric.pamassets.ClientLocalPam;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HarvestcraftfabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientLocalPam pam = new ClientLocalPam(Harvestcraftfabric.getInstance().getLocalPam());
        Artifice.registerAssets(new Identifier("harvestcraft", "harvestcraft"), pam::registerPamResources);

        Harvestcraftfabric.CROP_BLOCKS
                .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        Harvestcraftfabric.SAPLING_BLOCKS
                .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        Harvestcraftfabric.FRUIT_BLOCKS
                .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        Harvestcraftfabric.GARDEN_BLOCK
                .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));

        ScreenRegistry.<PresserGuiDescription, PresserScreen>register(GuiRegistry.PRESSER_HANDLER_TYPE, (gui, inv, title) -> new PresserScreen(gui, inv.player, title));
    }
}
