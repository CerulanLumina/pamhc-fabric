package net.cerulan.harvestcraftfabric.blockentity;

import net.cerulan.harvestcraftfabric.config.ConfigHandler;
import net.cerulan.harvestcraftfabric.config.GeneralConfig;
import net.cerulan.harvestcraftfabric.inventory.MachineInventory;
import net.cerulan.harvestcraftfabric.pamassets.LocalPam;
import net.cerulan.harvestcraftfabric.util.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class TrapBlockEntity extends BlockEntity implements
        Tickable,
        NamedScreenHandlerFactory {

    protected final MachineInventory inventory = new MachineInventory(19, IntStream.range(1, 19).toArray(), new int[]{0});

    protected int ticksProgress = 0;
    protected int ticksRequired = -1;

    private int ticksUntilCheck = CHECK_RATE_TICKS;
    private static final int CHECK_RATE_TICKS = 200;

    protected final Tag<Item> baitTag;

    private final MachineBlockEntity.GuiDescriptionFactory factory;
    private final String lootType;
    protected TrapBlockEntity(String lootType, Tag<Item> baitTag, BlockEntityType<?> type, MachineBlockEntity.GuiDescriptionFactory factory) {
        super(type);
        this.baitTag = baitTag;
        this.factory = factory;
        this.lootType = lootType;
    }

    @Override
    public void tick() {
        assert world != null;
        if (world.isClient()) return;
        if (ticksUntilCheck == CHECK_RATE_TICKS) {
            updateTicksRequired();
            ticksUntilCheck = 0;
        } else ++ticksUntilCheck;
        if (ticksRequired > 0) {
            if (hasBait()) {
                if (ticksProgress >= ticksRequired) {
                    ticksProgress = 0;
                    generateLoot();
                } else ++ticksProgress;
            } else ticksProgress = 0;
        }
    }

    protected boolean hasBait() {
        return baitTag.contains(inventory.getStack(0).getItem());
    }

    protected void generateLoot() {
        assert world != null;
        ItemStack bait = inventory.getStack(0);
        String[] parts = Registry.ITEM.getId(bait.getItem()).getPath().split("/");
        String path = parts[parts.length - 1];
        List<ItemStack> stacks = Objects.requireNonNull(world.getServer()).getLootManager()
                .getTable(LocalPam.modID("traps/" + lootType + "/" + path))
                .generateLoot(createLootContext((ServerWorld)world));
        stacks.forEach(stack -> InventoryUtil.insertInto(inventory, stack, (i) -> true, 1)); // startAt=1 means all slots are output in this case
        bait.decrement(1);
    }

    protected LootContext createLootContext(ServerWorld world) {
        return new LootContext.Builder(world).build(LootContextTypes.EMPTY);
    }

    private void updateTicksRequired() {

        assert world != null;
        long adjacent = Stream.of(pos.north(), pos.east(), pos.west(), pos.south())
                .filter(world::canSetBlock)
                .count();
        if (adjacent != 4) {
            ticksRequired = -1;
            return;
        }

        GeneralConfig.MachineConfig config = ConfigHandler.getGeneralConfig().machineConfig;
        long matching = BlockPos.stream(pos.north(2).west(2), pos.south(2).east(2))
                .map(BlockPos::toImmutable)
                .filter(p -> !pos.equals(p))
                .filter(world::canSetBlock)
                .map(world::getBlockState)
                .filter(this::blockPredicate)
                .count() - 4;
        double multiplier = Math.pow(config.trapRatio, matching);
        ticksRequired = (int)(config.trapBaseTicks * multiplier);
    }

    protected abstract boolean blockPredicate(BlockState state);

    public SidedInventory getInventory() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return factory.create(syncId, inv, ScreenHandlerContext.create(world, pos));
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

}
