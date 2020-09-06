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

    public static final PresserMachine PRESSER_MACHINE = new PresserMachine();

    public static void registerMachines() {
        registerMachine("presser", PRESSER_MACHINE);
    }

    private static void registerMachine(String localId, Block block) {
        Registry.register(Registry.BLOCK, new Identifier("harvestcraft", localId), block);
        Item item = new BlockItem(block, new Item.Settings().group(Harvestcraftfabric.getInstance().HARVESTCRAFT_FOOD_GROUP));
        Registry.register(Registry.ITEM, new Identifier("harvestcraft", localId), item);
    }

}
