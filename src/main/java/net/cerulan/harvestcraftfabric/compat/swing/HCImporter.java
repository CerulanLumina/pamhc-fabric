package net.cerulan.harvestcraftfabric.compat.swing;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class HCImporter {

    public static boolean tryOpenHC(boolean wasInvalid) {
        Optional<ModContainer> thisMod = FabricLoader.getInstance().getModContainer("harvestcraftfabric");
        assert thisMod.isPresent();
        String jarVersion = thisMod.get().getMetadata().getCustomValue("harvestcraftfabric:pam_version").getAsString();
        Path output = FabricLoader.getInstance().getGameDir().resolve("pamhc/harvestcraft.jar");
        if (wasInvalid) {
            try {
                Files.deleteIfExists(output);
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        if (!TinyFileDialogs.tinyfd_messageBox("Importing Pam's HarvestCraft",
                "HarvestCraft for Fabric requires Pam's HarvestCraft for Forge + 1.12.2 installed to function.\n" +
                        "Please download and select Pam's HarvestCraft " + jarVersion + ".\n" +
                        "Alternatively, you can manually place the .jar file at .minecraft/pamhc/harvestcraft.jar.",
                "okcancel", "info", true)) {
            return false;
        }
        MemoryStack stack = MemoryStack.stackPush();
        PointerBuffer filters = stack.mallocPointer(1);
        filters.put(stack.UTF8("*.jar"));
        filters.flip();

        String s = System.getProperty("user.home");
        Path defaultPath = Paths.get(s).toAbsolutePath();
        String defaultString = defaultPath.toString();
        if (defaultPath.toFile().isDirectory() && !defaultPath.endsWith(File.separator)) {
            defaultString += File.separator;
        }

        Optional<Path> res = Optional.ofNullable(TinyFileDialogs
                .tinyfd_openFileDialog("Import Pam's HarvestCraft for Forge / " + jarVersion,
                        defaultString,
                        filters,
                        "Pam's HarvestCraft " + jarVersion + " Jar",
                        false
                )).map(Paths::get);
        if (!res.isPresent()) {
            return false;
        }
        Path importing = res.get();
        try {
            Harvestcraftfabric.LOGGER.info("Importing HarvestCraft");
            Files.copy(importing, output, StandardCopyOption.REPLACE_EXISTING);
            stack.pop();
            return true;
        } catch (IOException ex) {
            stack.pop();
            return false;
        }
    }
}
