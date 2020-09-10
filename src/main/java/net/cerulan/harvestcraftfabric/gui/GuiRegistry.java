package net.cerulan.harvestcraftfabric.gui;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class GuiRegistry {

    public static ScreenHandlerType<PresserGuiDescription> PRESSER_HANDLER_TYPE;
    public static ScreenHandlerType<GrinderGuiDescription> GRINDER_HANDLER_TYPE;
    public static ScreenHandlerType<MarketGuiDescription> MARKET_HANDLER_TYPE;
    public static ScreenHandlerType<WaterFilterGuiDescription> WATERFILTER_HANDLER_TYPE;

    public static void registerScreenHandlers() {
        PRESSER_HANDLER_TYPE = register("presser", PresserGuiDescription::new);
        GRINDER_HANDLER_TYPE = register("grinder", GrinderGuiDescription::new);
        WATERFILTER_HANDLER_TYPE = register("waterfilter", WaterFilterGuiDescription::new);
        MARKET_HANDLER_TYPE = registerExtended("market", MarketGuiDescription::new);
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String localId, GuiDescriptionFactory factory) {
        return (ScreenHandlerType<T>) ScreenHandlerRegistry
                .registerSimple(new Identifier("harvestcraft", localId),
                        (syncId, inv) -> factory.create(syncId, inv, ScreenHandlerContext.EMPTY));
    }

    private static <T extends ScreenHandler> ScreenHandlerType<T> registerExtended(String localId, ExtendedGuiDescriptionFactory factory) {
        return (ScreenHandlerType<T>) ScreenHandlerRegistry
                .registerExtended(new Identifier("harvestcraft", localId),
                        (syncId, inv, buf) -> factory.create(syncId, inv, ScreenHandlerContext.EMPTY, buf));
    }

    @FunctionalInterface
    interface GuiDescriptionFactory<T extends ScreenHandler> {
        T create(int id, PlayerInventory inv, ScreenHandlerContext context);
    }

    @FunctionalInterface
    interface ExtendedGuiDescriptionFactory<T extends ScreenHandler> {
        T create(int id, PlayerInventory inv, ScreenHandlerContext context, PacketByteBuf buf);
    }

}
