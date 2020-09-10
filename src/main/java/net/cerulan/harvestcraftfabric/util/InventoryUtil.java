package net.cerulan.harvestcraftfabric.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.function.IntPredicate;

public class InventoryUtil {

    public static ItemStack insertInto(Inventory inv, ItemStack stack) {
        return insertInto(inv, stack, (i) -> true);
    }

    public static ItemStack insertInto(Inventory inv, ItemStack stack, IntPredicate slotPredicate, int startAt) {
        while (!stack.isEmpty()) {
            int intoSlot = getOccupiedSlotWithRoomForStack(inv, stack, slotPredicate, startAt);
            boolean empty = false;
            if (intoSlot == -1) {
                empty = true;
                intoSlot = getEmptySlot(inv, slotPredicate);
            }
            if (intoSlot == -1) {
                return stack;
            }
            if (!empty) {
                int putAmount = Math.min(inv.getMaxCountPerStack(), stack.getMaxCount()) - inv.getStack(intoSlot).getCount();
                stack.decrement(putAmount);
                inv.getStack(intoSlot).increment(putAmount);
            } else {
                inv.setStack(intoSlot, stack.copy());
                stack.setCount(0);
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack insertInto(Inventory inv, ItemStack stack, IntPredicate slotPredicate) {
        return insertInto(inv, stack, slotPredicate, 0);
    }

    public static int getOccupiedSlotWithRoomForStack(Inventory inventory, ItemStack stack) {
        return getOccupiedSlotWithRoomForStack(inventory, stack, (i) -> true);
    }

    public static int getOccupiedSlotWithRoomForStack(Inventory inventory, ItemStack stack, IntPredicate slotPredicate) {
        return getOccupiedSlotWithRoomForStack(inventory, stack, slotPredicate, 0);
    }

    public static int getOccupiedSlotWithRoomForStack(Inventory inventory, ItemStack stack, IntPredicate slotPredicate, int startAt) {
        for(int i = startAt; i < inventory.size(); ++i) {
            if (!slotPredicate.test(i)) continue;
            if (canStackAddMore(inventory, inventory.getStack(i), stack)) {
                return i;
            }
        }

        return -1;
    }

    public static int getEmptySlot(Inventory inv) {
        return getEmptySlot(inv, (i) -> true);
    }

    public static int getEmptySlot(Inventory inv, IntPredicate slotPredicate) {
        return getEmptySlot(inv, slotPredicate, 0);
    }

    public static int getEmptySlot(Inventory inv, IntPredicate slotPredicate, int startAt) {
        for(int i = startAt; i < inv.size(); ++i) {
            if (!slotPredicate.test(i)) continue;
            if (inv.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static boolean canStackAddMore(Inventory inv, ItemStack existingStack, ItemStack stack) {
        return canStackAddMore(existingStack, stack, inv.getMaxCountPerStack());
    }

    public static boolean canStackAddMore(ItemStack existingStack, ItemStack stack, int stackSize) {
        return !existingStack.isEmpty()
                && areItemsEqual(existingStack, stack)
                && existingStack.isStackable()
                && existingStack.getCount() < existingStack.getMaxCount()
                && existingStack.getCount() < stackSize;
    }

    public static boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        return canStackAddMore(existingStack, stack, 64);
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areTagsEqual(stack1, stack2);
    }

}
