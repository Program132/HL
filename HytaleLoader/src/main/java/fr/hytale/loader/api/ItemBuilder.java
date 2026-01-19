package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating custom ItemStacks with metadata.
 * <p>
 * This builder provides a fluent interface for creating items with custom
 * names,
 * lore, durability, and other properties.
 * </p>
 * 
 * <h3>Usage Example:</h3>
 * 
 * <pre>{@code
 * Item customSword = new ItemBuilder("Weapon_Sword_Iron")
 *         .quantity(1)
 *         .displayName(ChatColor.GOLD + "Legendary Sword")
 *         .lore(ChatColor.GRAY + "A powerful weapon")
 *         .lore(ChatColor.GRAY + "Forged in ancient times")
 *         .durability(100.0)
 *         .build();
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public class ItemBuilder {

    private final String itemId;
    private int quantity = 1;
    private String displayName = null;
    private final List<String> lore = new ArrayList<>();
    private Double durability = null;
    private Double maxDurability = null;

    /**
     * Creates a new ItemBuilder for the specified item ID.
     * 
     * @param itemId the item ID (e.g., "Weapon_Sword_Iron")
     */
    public ItemBuilder(@Nonnull String itemId) {
        this.itemId = itemId;
    }

    /**
     * Sets the quantity of the item.
     * 
     * @param quantity the quantity (must be > 0)
     * @return this builder for chaining
     */
    public ItemBuilder quantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
        return this;
    }

    /**
     * Sets the display name of the item.
     * <p>
     * Supports Minecraft color codes with '&' prefix or ChatColor constants.
     * </p>
     * 
     * @param displayName the display name
     * @return this builder for chaining
     */
    public ItemBuilder displayName(@Nonnull String displayName) {
        this.displayName = ChatColor.translateAlternateColorCodes(displayName);
        return this;
    }

    /**
     * Adds a lore line to the item.
     * <p>
     * Supports Minecraft color codes with '&' prefix or ChatColor constants.
     * Can be called multiple times to add multiple lines.
     * </p>
     * 
     * @param loreLine the lore line to add
     * @return this builder for chaining
     */
    public ItemBuilder lore(@Nonnull String loreLine) {
        this.lore.add(ChatColor.translateAlternateColorCodes(loreLine));
        return this;
    }

    /**
     * Sets the lore of the item (replaces existing lore).
     * 
     * @param loreLines the lore lines
     * @return this builder for chaining
     */
    public ItemBuilder lore(@Nonnull List<String> loreLines) {
        this.lore.clear();
        for (String line : loreLines) {
            this.lore.add(ChatColor.translateAlternateColorCodes(line));
        }
        return this;
    }

    /**
     * Sets the current durability of the item.
     * 
     * @param durability the durability value
     * @return this builder for chaining
     */
    public ItemBuilder durability(double durability) {
        this.durability = durability;
        return this;
    }

    /**
     * Sets the maximum durability of the item.
     * 
     * @param maxDurability the maximum durability
     * @return this builder for chaining
     */
    public ItemBuilder maxDurability(double maxDurability) {
        this.maxDurability = maxDurability;
        return this;
    }

    /**
     * Builds the Item with all configured properties.
     * 
     * @return the constructed Item
     */
    public Item build() {
        // Create metadata if needed
        BsonDocument metadata = null;
        if (displayName != null || !lore.isEmpty()) {
            metadata = new BsonDocument();

            // Set display name
            if (displayName != null) {
                metadata.put("displayName", new BsonString(displayName));
            }

            // Set lore
            if (!lore.isEmpty()) {
                BsonArray loreArray = new BsonArray();
                for (String line : lore) {
                    loreArray.add(new BsonString(line));
                }
                metadata.put("lore", loreArray);
            }
        }

        // Create ItemStack with appropriate constructor
        ItemStack itemStack;

        if (durability != null && maxDurability != null) {
            // Constructor: ItemStack(String itemId, int quantity, double durability, double
            // maxDurability, BsonDocument metadata)
            itemStack = new ItemStack(itemId, quantity, durability, maxDurability, metadata);
        } else {
            // Constructor: ItemStack(String itemId, int quantity, BsonDocument metadata)
            itemStack = new ItemStack(itemId, quantity, metadata);
        }

        return new Item(itemStack);
    }
}
