package fr.hytale.loader.api.inventory;

import fr.hytale.loader.api.Player;

/**
 * HytaleLoader wrapper for a player's inventory.
 * <p>
 * This class provides access to a player's complete inventory, including
 * their storage slots, hotbar, and armor slots. It extends the base
 * {@link Inventory} class with player-specific functionality.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.1
 */
public class InventoryPlayer extends Inventory {
    /**
     * Constructs a new Inventory wrapper for a player.
     *
     * @param player the player instance from Hytale Loader API
     */
    public InventoryPlayer(Player player) {
        super(player.getNativePlayer().getInventory());
    }
}
