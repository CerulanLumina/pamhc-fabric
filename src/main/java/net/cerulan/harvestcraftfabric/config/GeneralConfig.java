package net.cerulan.harvestcraftfabric.config;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "harvestcraft")
public class GeneralConfig implements ConfigData {

    @Comment("Whether to add crop seeds to grass, along wheat seeds.")
    public boolean addCropSeedsToGrass = false;

    @Comment("Whether to generate gardens that drop grown crop items in the world.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean generateCropGardens = true;

    @Comment("Whether to generate fruit trees in the world.")
    @ConfigEntry.Gui.RequiresRestart
    public boolean generateTrees = true;

    @ConfigEntry.Gui.CollapsibleObject
    public MachineConfig machineConfig = new MachineConfig();

    public static class MachineConfig {

        @Comment("Whether the market is enabled")
        @ConfigEntry.Gui.RequiresRestart
        public boolean enableMarket = true;

        @Comment("Whether the shipping bin is enabled")
        @ConfigEntry.Gui.RequiresRestart
        public boolean enableShippingBin = true;

        @Comment("How long, in ticks, the Presser takes per operation")
        public int presserTicks = 125;

        @Comment("How long, in ticks, the Grinder takes per operation")
        public int grinderTicks = 125;

        @Comment("How long, in ticks, the Water Filter takes per operation")
        public int waterFilterTicks = 500;

        @Comment("The base amount of ticks traps take to produce loot prior to speedups by providing more of the necessary block")
        public int trapBaseTicks = 3500;

        @Comment("The base rate of the Ground and Water trap will be multiplied by this amount for EACH additional block in a 5x5 area centered on the trap")
        public double trapRatio = 0.97;

    }

}
