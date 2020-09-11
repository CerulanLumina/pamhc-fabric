package net.cerulan.harvestcraftfabric;

import net.cerulan.harvestcraftfabric.compat.swing.HCImporter;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.gui.FabricGuiEntry;

import java.util.Optional;

public class PreLaunchEntry implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        while (true) {
            try {
                LocalPam.verifyLocalFiles(LocalPam.HARVEST_JAR_PATH);
                break;
            } catch (LocalPam.InvalidHarvestCraftException ex) {
                tryDispatchChooser(ex, true);
            } catch (LocalPam.MissingHarvestCraftException ex) {
                tryDispatchChooser(ex);
            }
        }
    }

    private void tryDispatchChooser(LocalPam.MissingHarvestCraftException ex) {
        tryDispatchChooser(ex, false);
    }

    private void tryDispatchChooser(LocalPam.MissingHarvestCraftException ex, boolean invalid) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class.forName("org.lwjgl.util.tinyfd.TinyFileDialogs");
                if (!HCImporter.tryOpenHC(invalid)) {
                    FabricGuiEntry.displayCriticalError(ex, true);
                }
            } catch (ClassNotFoundException ignored) {
                fallbackEmitErrorLog(ex);
            }
        } else {
            fallbackEmitErrorLog(ex);
        }
    }

    private void fallbackEmitErrorLog(LocalPam.MissingHarvestCraftException ex) {
        Optional<ModContainer> thisMod = FabricLoader.getInstance().getModContainer("harvestcraftfabric");
        assert thisMod.isPresent();
        String jarVersion = thisMod.get().getMetadata().getCustomValue("harvestcraftfabric:pam_version").getAsString();
        Harvestcraftfabric.LOGGER.error(ex.getMessage());
        Harvestcraftfabric.LOGGER.error("HarvestCraft for Fabric requires Pam's HarvestCraft for Forge + 1.12.2 installed to function.");
        Harvestcraftfabric.LOGGER.error("Please download Pam's HarvestCraft " + jarVersion + ", and place it at " + System.getProperty("user.dir") + " /pamhc/harvestcraft.jar");
        System.exit(1);
    }
}
