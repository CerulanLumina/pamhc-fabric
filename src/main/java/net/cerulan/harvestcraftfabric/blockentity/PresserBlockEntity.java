package net.cerulan.harvestcraftfabric.blockentity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.cerulan.harvestcraftfabric.gui.PresserGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.recipe.DoubleOutputRecipe;
import net.cerulan.harvestcraftfabric.recipe.RecipeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
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

import java.util.Optional;

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
                case 0:
                    return ticksProgress;
                case 1:
                    return MAXIMUM_PROGRESS;
                default:
                    throw new UnsupportedOperationException();
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
        assert world != null;
        if (world.isClient) return;
        Optional<DoubleOutputRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeRegistry.PRESSER, this.inventory, world);
        if (recipe.isPresent()) {
            ItemStack output = recipe.get().getOutput();
            ItemStack output2 = recipe.get().getOutput2();
            if (canOutput(output, 1) && canOutput(output2, 2)) {
                if (ticksProgress == MAXIMUM_PROGRESS) {
                    ticksProgress = 0;
                    if (inventory.getStack(1).isEmpty()) inventory.setStack(1, output);
                    else inventory.getStack(1).increment(output.getCount());
                    if (inventory.getStack(2).isEmpty()) inventory.setStack(2, output2);
                    else
                        inventory.getStack(2).increment(output2.getCount());
                    inventory.getStack(0).decrement(1);
                } else ++ticksProgress;
            } else ticksProgress = 0;
        }
        else ticksProgress = 0;
    }

    private boolean canOutput(ItemStack stack, int slot) {
        ItemStack existing = inventory.getStack(slot);
        return existing.isEmpty() || (existing.isItemEqual(stack) && ItemStack.areTagsEqual(existing, stack) && existing.getCount() + stack.getCount() <= stack.getMaxCount());
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
