package net.cerulan.harvestcraftfabric.blockentity;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.cerulan.harvestcraftfabric.gui.ShippingBinGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;

import java.util.stream.IntStream;

public class ShippingBinBE extends BlockEntity implements
        Tickable,
        NamedScreenHandlerFactory {

    private final MachineInventory inventory;
    public ShippingBinBE() {
        super(ModBlockEntities.SHIPPING_BIN_BE_TYPE);
        this.inventory = new MachineInventory(10, new int[] { 0 }, IntStream.range(1, 10).toArray());
    }

    @Override
    public void tick() {

    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ShippingBinGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    public SidedInventory getInventory() {
        return inventory;
    }
}
