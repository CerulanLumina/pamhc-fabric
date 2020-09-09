package net.cerulan.harvestcraftfabric.client;

import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.gui.GrinderGuiDescription;
import net.cerulan.harvestcraftfabric.gui.GuiRegistry;
import net.cerulan.harvestcraftfabric.gui.MarketGuiDescription;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.cerulan.harvestcraftfabric.gui.client.GrinderScreen;
import net.cerulan.harvestcraftfabric.gui.client.MarketScreen;
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


        Harvestcraftfabric.getInstance().getCutoutBlocks()
                .forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));

        ScreenRegistry.<PresserGuiDescription, PresserScreen>register(GuiRegistry.PRESSER_HANDLER_TYPE, (gui, inv, title) -> new PresserScreen(gui, inv.player, title));
        ScreenRegistry.<GrinderGuiDescription, GrinderScreen>register(GuiRegistry.GRINDER_HANDLER_TYPE, (gui, inv, title) -> new GrinderScreen(gui, inv.player, title));
        ScreenRegistry.<MarketGuiDescription, MarketScreen>register(GuiRegistry.MARKET_HANDLER_TYPE, (gui, inv, title) -> new MarketScreen(gui, inv.player, title));
    }
}
