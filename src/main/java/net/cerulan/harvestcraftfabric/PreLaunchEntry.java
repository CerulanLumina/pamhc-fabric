package net.cerulan.harvestcraftfabric;

import net.cerulan.harvestcraftfabric.compat.swing.HCImporter;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.gui.FabricGuiEntry;

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
        if (!HCImporter.tryOpenHC(invalid)) {
            FabricGuiEntry.displayCriticalError(ex, true);
        }
    }
}
