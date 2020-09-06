package net.cerulan.harvestcraftfabric.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class PresserBlockEntity extends BlockEntity implements Tickable,
        InventoryProvider,
        PropertyDelegateHolder,
        NamedScreenHandlerFactory {

    private final MachineInventory inventory;

    private int ticksProgress = 0;
    private static final int MAXIMUM_PROGRESS = 200;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0: return ticksProgress;
                case 1: return MAXIMUM_PROGRESS;
                default: throw new UnsupportedOperationException();
            }
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                ticksProgress = value;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PresserBlockEntity() {
        super(ModBlockEntities.PRESSER_BE_TYPE);
        inventory = new MachineInventory();
    }

    @Override
    public void tick() {
        if (ticksProgress == MAXIMUM_PROGRESS) ticksProgress = 0;
        else ++ticksProgress;
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return inventory;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invTag = new CompoundTag();
        Inventories.toTag(invTag, inventory.getItems());
        tag.put("items", invTag);
        tag.putInt("progress", ticksProgress);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (tag.contains("items", 10))
            Inventories.fromTag(tag.getCompound("items"), inventory.getItems());
        if (tag.contains("progress"))
            ticksProgress = tag.getInt("progress");
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new PresserGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
