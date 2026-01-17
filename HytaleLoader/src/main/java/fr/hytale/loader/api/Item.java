package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.inventory.ItemStack;

/**
 * HytaleLoader wrapper for the native Hytale ItemStack class.
 * <p>
 * This class provides a simplified API for interacting with items.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.1
 */
public class Item {

    private final ItemStack nativeItemStack;

    /**
     * Constructs a new Item wrapper from an existing ItemStack.
     * 
     * @param nativeItemStack the native Hytale item stack instance
     */
    public Item(ItemStack nativeItemStack) {
        this.nativeItemStack = nativeItemStack;
    }

    /**
     * Constructs a new Item with the specified ID and quantity.
     * 
     * @param id       the item ID (e.g., "hytale:sword")
     * @param quantity the quantity
     */
    public Item(String id, int quantity) {
        this.nativeItemStack = new ItemStack(id, quantity);
    }

    /**
     * Gets the native Hytale item stack instance.
     * 
     * @return the wrapped native item stack
     */
    public ItemStack getNativeItemStack() {
        return nativeItemStack;
    }

    /**
     * Gets the item's ID.
     * 
     * @return the item ID
     */
    public String getId() {
        return nativeItemStack != null ? nativeItemStack.getItemId() : null;
    }

    /**
     * Gets the item's quantity.
     * 
     * @return the quantity
     */
    public int getQuantity() {
        return nativeItemStack != null ? nativeItemStack.getQuantity() : 0;
    }

    /**
     * Checks if the item is broken.
     * 
     * @return true if the item is broken, false otherwise
     */
    public boolean isBroken() {
        return this.nativeItemStack.isBroken();
    }

    /**
     * Gets the maximum durability of the item.
     * 
     * @return the maximum durability
     */
    public double maxDurability() {
        return this.nativeItemStack.getMaxDurability();
    }

    /**
     * Gets the block key associated with this item, if any.
     * 
     * @return the block key, or null if not applicable
     */
    public String getBlockKey() {
        return this.nativeItemStack.getBlockKey();
    }

    /**
     * Checks if this item is of an equivalent type to another item.
     * <p>
     * This checks if the item IDs match, ignoring quantity and other metadata.
     * </p>
     * 
     * @param item the other item to compare with
     * @return true if the items are of equivalent type
     */
    public boolean isEquivalentType(Item item) {
        return item != null && this.nativeItemStack.isEquivalentType(item.getNativeItemStack());
    }

    @Override
    public String toString() {
        return "{ITEM=" + getId() + " | QTY=" + getQuantity() + "}";
    }
}
