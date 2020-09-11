package net.cerulan.harvestcraftfabric.compat.modmenu;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.cerulan.harvestcraftfabric.config.GeneralConfig;

public class HarvestcraftModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(GeneralConfig.class, parent).get();
    }

}
