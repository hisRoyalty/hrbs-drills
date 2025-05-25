package com.hisroyalty.hrbsdrills.container;

import com.hisroyalty.hrbsdrills.DrillContainers;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StorageContainer extends AbstractContainerMenu {

    public final int size;
    public final String chestType;

    public StorageContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, buffer.readUtf(32767));
    }

    private StorageContainer(int id, Inventory playerInventory, String chestType) {
        this(id, playerInventory, new ItemStackHandler(27), chestType);
    }

    public StorageContainer(int id, Inventory playerInventory, IItemHandler itemHandler, String chestType) {
        super(DrillContainers.STORAGE_CONTAINER.get(), id);
        checkContainerSize(playerInventory, itemHandler.getSlots());
        size = itemHandler.getSlots();
        this.chestType = chestType;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        Entity entity = playerIn.getVehicle();
        if (entity instanceof DrillEntity e && entity.isAlive()) {
            return e.isAlive();
        }

        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < size) {
                if (!this.moveItemStackTo(itemstack1, size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, size, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}

