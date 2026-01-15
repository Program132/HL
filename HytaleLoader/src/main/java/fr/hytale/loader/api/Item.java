package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.inventory.ItemStack;

/**
 * HytaleLoader wrapper for the native Hytale ItemStack class.
 * <p>
 * This class provides a simplified API for interacting with items.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
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

    public boolean isBroken() {
        return this.nativeItemStack.isBroken();
    }

    public double maxDurability() {
        return this.nativeItemStack.getMaxDurability();
    }

    public String getBlockKey() {
        return this.nativeItemStack.getBlockKey();
    }

    public boolean isEquivalentType(Item item) {
        return this.nativeItemStack.isEquivalentType(item.getNativeItemStack());
    }

    public String toString() {
        return "{ITEM=" + getId() + "|" + " | " + getQuantity() + "}";
    }
}
