package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.cerulan.harvestcraftfabric.blockentity.GroundTrapBE;
import net.cerulan.harvestcraftfabric.blockentity.WaterTrapBE;
import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MachineRegistry {

    public static final DirectionProperty FACING_PROPERTY = DirectionProperty.of("facing", dir -> dir.getAxis().isHorizontal());

    public static final PresserMachineBlock PRESSER_MACHINE = new PresserMachineBlock();
    public static final GrinderMachineBlock GRINDER_MACHINE = new GrinderMachineBlock();
    public static final MarketBlock MARKET_BLOCK = new MarketBlock();
    public static final WaterFilterBlock WATER_FILTER_BLOCK = new WaterFilterBlock();
    public static final TrapBlock WATER_TRAP_BLOCK = new TrapBlock(WaterTrapBE::new);
    public static final TrapBlock GROUND_TRAP_BLOCK = new TrapBlock(GroundTrapBE::new);
    public static final ShippingBinBlock SHIPPING_BIN_BLOCK = new ShippingBinBlock();

    public static void registerMachines() {
        registerMachine("presser", PRESSER_MACHINE);
        registerMachine("grinder", GRINDER_MACHINE);
        registerMachine("waterfilter", WATER_FILTER_BLOCK);
        registerMachine("watertrap", WATER_TRAP_BLOCK);
        registerMachine("groundtrap", GROUND_TRAP_BLOCK);
        if (ConfigHandler.getGeneralConfig().machineConfig.enableMarket)
            registerMachine("market", MARKET_BLOCK);
        if (ConfigHandler.getGeneralConfig().machineConfig.enableShippingBin)
            registerMachine("shippingbin", SHIPPING_BIN_BLOCK);
    }

    private static void registerMachine(String localId, Block block) {
        Registry.register(Registry.BLOCK, new Identifier("harvestcraft", localId), block);
        Item item = new BlockItem(block, new Item.Settings().group(Harvestcraftfabric.getInstance().harvestcraftFoodGroup));
        Registry.register(Registry.ITEM, new Identifier("harvestcraft", localId), item);
    }

}
