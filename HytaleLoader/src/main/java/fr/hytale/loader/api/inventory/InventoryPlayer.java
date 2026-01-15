package fr.hytale.loader.api.inventory;

import fr.hytale.loader.api.Player;

public class InventoryPlayer extends Inventory {
    /**
     * Constructs a new Inventory wrapper for a player
     *
     * @param player the player instance from Hytale Loader API
     */
    public InventoryPlayer(Player player) {
        super(player.getNativePlayer().getInventory());
    }
}
