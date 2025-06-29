package com.hisroyalty.hrbsdrills.container;

import com.hisroyalty.hrbsdrills.DrillContainers;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;




public class DrillContainer extends AbstractContainerMenu {

    public DrillEntity drillEntity;
    private int storageSlotStart = -1;
    private int storageSlotCount = 0;

    public DrillContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, (DrillEntity) playerInventory.player.level().getEntity(buffer.readVarInt()));
    }

    public DrillContainer(int id, Inventory playerInventory, DrillEntity drillEntity) {
        super(DrillContainers.DRILL_CONTAINER.get(), id);
        checkContainerSize(playerInventory, 3);
        this.drillEntity = drillEntity;

        initSlots(playerInventory);
    }

    private void initSlots(Inventory playerInventory) {
        if (this.drillEntity != null) {
            this.drillEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                this.addSlot(new SlotItemHandler(iItemHandler, 2, 62, 7)); // Drill head
                this.addSlot(new SlotItemHandler(iItemHandler, 0, 26, 8)); // Water/Ice
                this.addSlot(new SlotItemHandler(iItemHandler, 1, 126, 37)); // Fuel
            });
        }

        int yOffset = 0;
        if (drillEntity!=null) yOffset = this.drillEntity.getHasChestUpgrade() ? 26 : -36;

            // Storage upgrade slots (if present)
        if (this.drillEntity != null && this.drillEntity.getHasChestUpgrade()) {
            this.drillEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
                storageSlotStart = this.slots.size();
                storageSlotCount = 27;
                int baseX = 8;
                int baseY = 70;
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 9; ++j) {
                        this.addSlot(new SlotItemHandler(iItemHandler, 3 + j + i * 9, baseX + j * 18, baseY + i * 18));
                    }
                }
            });
        }

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 112 + i * 18 + yOffset));
            }
        }

        // Player hotbar
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 170 + yOffset));
        }
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            int storageEnd = storageSlotStart + storageSlotCount;
            if (storageSlotStart >= 0 && pIndex >= storageSlotStart && pIndex < storageEnd) {
                if (!moveItemStackTo(sourceStack, 0, storageSlotStart, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (storageSlotStart >= 0 && pIndex < storageSlotStart) {
                if (!moveItemStackTo(sourceStack, storageSlotStart, storageEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                System.out.println("Invalid slotIndex:" + pIndex);
                return ItemStack.EMPTY;
            }
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getVehicle() instanceof DrillEntity;
    }

    public DrillEntity getDrillEntity() {
        return drillEntity;
    }

}
