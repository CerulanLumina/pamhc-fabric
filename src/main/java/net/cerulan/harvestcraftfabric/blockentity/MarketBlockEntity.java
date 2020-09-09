package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.gui.MarketGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.CallbackMachineInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class MarketBlockEntity extends BlockEntity
        implements InventoryProvider,
        Tickable,
        ExtendedScreenHandlerFactory {

    private final CallbackMachineInventory inventory;
    private ItemStack output = ItemStack.EMPTY;

    public MarketBlockEntity() {
        super(ModBlockEntities.MARKET_BE_TYPE);
        this.inventory = new CallbackMachineInventory(2, new int[]{1}, new int[]{0});
    }

    @Override
    public void tick() {
        if (inventory.getStack(0).getItem() == Items.EMERALD && inventory.getStack(1).isEmpty() && !output.isEmpty()) {
            inventory.setStack(1, output.copy());
            inventory.getStack(0).decrement(1);
        }
    }

    public void setOutput(ItemStack stack) {
        this.output = stack;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invTag = new CompoundTag();
        Inventories.toTag(invTag, inventory.getItems());
        tag.put("items", invTag);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (tag.contains("items", 10))
            Inventories.fromTag(tag.getCompound("items"), inventory.getItems());
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MarketGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }


    @Override
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeBlockPos(pos);
    }
}
