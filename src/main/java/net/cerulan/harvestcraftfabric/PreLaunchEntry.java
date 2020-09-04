package net.cerulan.harvestcraftfabric;

import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.gui.FabricGuiEntry;

public class PreLaunchEntry implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            LocalPam.verifyLocalFiles(LocalPam.HARVEST_JAR_PATH);
        } catch (LocalPam.MissingHarvestCraftException ex) {
            FabricGuiEntry.displayCriticalError(ex, true);
        }
    }
}
