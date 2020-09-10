package net.cerulan.harvestcraftfabric.blockentity;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.cerulan.harvestcraftfabric.gui.ShippingBinGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ShippingBinBE extends BlockEntity implements
        Tickable,
        NamedScreenHandlerFactory {

    private final MachineInventory inventory;
    private final Tag<Item> sellableTag;
    private static final int SELL_AMOUNT = 32;
    public ShippingBinBE() {
        super(ModBlockEntities.SHIPPING_BIN_BE_TYPE);
        this.sellableTag = TagRegistry.item(LocalPam.modID("sellable"));
        this.inventory = new MachineInventory(10, new int[] { 0 }, IntStream.range(1, 10).toArray());
    }

    @Override
    public void tick() {
        Arrays.stream(inventory.getAvailableSlots(null))
                .mapToObj(inventory::getStack)
                .filter(stack -> sellableTag.contains(stack.getItem()) && stack.getCount() >= SELL_AMOUNT)
                .forEach(this::payout);
    }

    private void payout(ItemStack spending) {
        spending.decrement(32);
        if (inventory.getStack(0).isEmpty()) {
            inventory.setStack(0, new ItemStack(Items.EMERALD));
        } else inventory.getStack(0).increment(1);
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
