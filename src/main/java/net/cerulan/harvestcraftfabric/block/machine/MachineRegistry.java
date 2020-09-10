package net.cerulan.harvestcraftfabric.block.machine;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
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

    public static void registerMachines() {
        registerMachine("presser", PRESSER_MACHINE);
        registerMachine("grinder", GRINDER_MACHINE);
        registerMachine("market", MARKET_BLOCK);
        registerMachine("waterfilter", WATER_FILTER_BLOCK);
    }

    private static void registerMachine(String localId, Block block) {
        Registry.register(Registry.BLOCK, new Identifier("harvestcraft", localId), block);
        Item item = new BlockItem(block, new Item.Settings().group(Harvestcraftfabric.getInstance().harvestcraftFoodGroup));
        Registry.register(Registry.ITEM, new Identifier("harvestcraft", localId), item);
    }

}
