package net.cerulan.harvestcraftfabric.client;

import com.swordglowsblue.artifice.api.Artifice;
import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.pamassets.ClientLocalPam;
import net.cerulan.harvestcraftfabric.pamassets.artifice.DataResource;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
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
    }
}
