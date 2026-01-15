package fr.hytale.loader.api.inventory;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * HytaleLoader wrapper for the native Hytale Inventory class.
 * <p>
 * This class provides a simplified API for interacting with inventories.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.1
 */
public class Inventory {

    private final com.hypixel.hytale.server.core.inventory.Inventory nativeInventory;

    /**
     * Constructs a new Inventory wrapper.
     * 
     * @param nativeInventory the native Hytale inventory instance
     */
    public Inventory(com.hypixel.hytale.server.core.inventory.Inventory nativeInventory) {
        this.nativeInventory = nativeInventory;
    }

    /**
     * Gets the native Hytale inventory instance.
     * 
     * @return the wrapped native inventory
     */
    public com.hypixel.hytale.server.core.inventory.Inventory getNativeInventory() {
        return nativeInventory;
    }

    /**
     * Clears the inventory.
     * <p>
     * Note: This method manually clears each slot to avoid issues with the native
     * clear() method
     * which may trigger events before the player is fully initialized in the world.
     * </p>
     */
    public void clear() {
        com.hypixel.hytale.server.core.inventory.Inventory inv = this.getNativeInventory();
        inv.clear();
    }

    /**
     * Adds an item to the inventory.
     * 
     * @param item the item to add
     */
    public void addItem(Item item) {
        if (nativeInventory != null && item != null) {
            nativeInventory.getStorage().addItemStack(item.getNativeItemStack());
        }
    }

    /**
     * Return the item at the slot given
     *
     * @param slot the slot of the inventory
     * @return item the item at the slot given
     */
    public Item getItem(int slot) {
        if (nativeInventory != null) {
            if (slot >= 9 && slot < nativeInventory.getStorage().getCapacity()) {
                ItemStack item = nativeInventory.getStorage().getItemStack((short) slot);
                if (item != null && !item.isEmpty()) {
                    return new Item(item);
                }
            } else if (slot >= 0 && slot < 9) {
                ItemStack item = nativeInventory.getHotbar().getItemStack((short) slot);
                if (item != null && !item.isEmpty()) {
                    return new Item(item);
                }
            }

        }
        return null;
    }

    /**
     * Gets the items in the inventory.
     * 
     * @return a list of items
     */
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        if (nativeInventory != null) {
            for (short i = 0; i < nativeInventory.getStorage().getCapacity(); i++) {
                ItemStack nativeItem = nativeInventory.getStorage().getItemStack(i);
                if (nativeItem != null && !nativeItem.isEmpty()) {
                    items.add(new Item(nativeItem));
                }
            }

            for (short i = 0; i < nativeInventory.getHotbar().getCapacity(); i++) {
                ItemStack nativeItem = nativeInventory.getHotbar().getItemStack(i);
                if (nativeItem != null && !nativeItem.isEmpty()) {
                    items.add(new Item(nativeItem));
                }
            }

            for (short i = 0; i < nativeInventory.getArmor().getCapacity(); i++) {
                ItemStack nativeItem = nativeInventory.getArmor().getItemStack(i);
                if (nativeItem != null && !nativeItem.isEmpty()) {
                    items.add(new Item(nativeItem));
                }
            }
        }
        return items;
    }

    /**
     * Sets the item at the specified slot in the inventory.
     * <p>
     * This method automatically determines whether to place the item in the hotbar
     * (slots 0-8)
     * or the main storage (slots 9+) based on the slot index provided.
     * </p>
     * 
     * @param item the item to place in the inventory
     * @param slot the target slot index (0-8 for hotbar, 9+ for storage)
     */
    public void setItem(Item item, int slot) {
        com.hypixel.hytale.server.core.inventory.Inventory inv = this.nativeInventory;
        if (inv != null) {
            if (slot >= 9 && slot < inv.getStorage().getCapacity()) {
                inv.getStorage().setItemStackForSlot((short) slot, item.getNativeItemStack());
            } else if (slot >= 0 && slot < 9) {
                inv.getHotbar().setItemStackForSlot((short) slot, item.getNativeItemStack());
            }
        }
    }
}
