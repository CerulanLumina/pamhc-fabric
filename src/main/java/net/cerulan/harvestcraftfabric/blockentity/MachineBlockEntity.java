package net.cerulan.harvestcraftfabric.blockentity;

import com.google.common.collect.ImmutableList;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;
import java.util.Optional;

public abstract class MachineBlockEntity<R extends Recipe<Inventory>> extends BlockEntity implements
        Tickable,
        InventoryProvider,
        PropertyDelegateHolder,
        NamedScreenHandlerFactory {

    protected final MachineInventory inventory;

    protected int ticksProgress = 0;
    private final GuiDescriptionFactory guiDescriptionFactory;

    protected MachineBlockEntity(BlockEntityType<? extends BlockEntity> type, RecipeType<R> recipeType, MachineInventory inventory, GuiDescriptionFactory guiDescriptionFactory) {
        super(type);
        this.recipeType = recipeType;
        this.inventory = inventory;
        this.guiDescriptionFactory = guiDescriptionFactory;
    }

    protected abstract int getMaximumProgress();

    protected abstract boolean canOutput(R recipe);

    protected abstract ImmutableList<ItemStack> getOutputsForRecipe(R recipe);

    protected abstract int[] getOutputSlots();

    protected abstract int[] getInputSlots();

    protected RecipeType<R> recipeType;

    @Override
    public void tick() {
        assert world != null;
        if (world.isClient) return;
        Optional<R> recipe = world.getRecipeManager().getFirstMatch(recipeType, this.inventory, world);
        if (recipe.isPresent()) {
            if (canOutput(recipe.get())) {
                if (ticksProgress == getMaximumProgress()) {
                    ticksProgress = 0;
                    int[] out = getOutputSlots();
                    ImmutableList<ItemStack> outputs = getOutputsForRecipe(recipe.get());
                    if (outputs.size() != out.length) {
                        throw new IllegalStateException("Outputs list was not the same size as output slot array");
                    }
                    int i = 0;
                    for (ItemStack stack : outputs) {
                        if (inventory.getStack(out[i]).isEmpty()) inventory.setStack(out[i], stack);
                        else inventory.getStack(out[i]).increment(stack.getCount());
                        ++i;
                    }
                    Arrays.stream(getInputSlots()).mapToObj(inventory::getStack).forEach(d -> d.decrement(1));
                } else ++ticksProgress;
            } else ticksProgress = 0;
        }
        else ticksProgress = 0;
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
        return guiDescriptionFactory.create(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @FunctionalInterface
    protected interface GuiDescriptionFactory {
        SyncedGuiDescription create(int syncId, PlayerInventory inv, ScreenHandlerContext context);
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    protected int getDelegatedProperty(int i) {
        throw new UnsupportedOperationException();
    }

    protected void setDelegatedProperty(int index, int val) {
        throw new UnsupportedOperationException();
    }

    protected int getAdditionalPropertiesCount() {
        return 0;
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return ticksProgress;
                case 1:
                    return getMaximumProgress();
                default:
                    return getDelegatedProperty(index);
            }
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                ticksProgress = value;
            } else {
                setDelegatedProperty(index, value);
            }
        }

        @Override
        public int size() {
            return 2 + getAdditionalPropertiesCount();
        }
    };

    public static boolean isMachineBE(BlockEntity entity) {
        return MachineBlockEntity.class.isAssignableFrom(entity.getClass());
    }
}
