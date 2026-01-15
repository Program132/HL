package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.entity.entities.Player;

/**
 * Called when a player crafts an item.
 * <p>
 * This event wraps the native Hytale PlayerCraftEvent and provides
 * simplified access to crafting information including the recipe and quantity.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> This event wraps a deprecated Hytale API and may
 * be replaced in future versions.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.0
 * @deprecated The underlying Hytale event is deprecated
 */
@Deprecated
public class PlayerCraftEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.player.PlayerCraftEvent originalEvent;

    public PlayerCraftEvent(com.hypixel.hytale.server.core.event.events.player.PlayerCraftEvent originalEvent) {
        this.originalEvent = originalEvent;
    }

    public Player getPlayer() {
        return originalEvent.getPlayer();
    }

    public CraftingRecipe getCraftedRecipe() {
        return originalEvent.getCraftedRecipe();
    }

    public int getQuantity() {
        return originalEvent.getQuantity();
    }

    public String getRecipeName() {
        return getCraftedRecipe() != null ? getCraftedRecipe().getId() : "Unknown";
    }

    public String getPlayerName() {
        return originalEvent.getPlayer() != null ? originalEvent.getPlayer().toString() : "Unknown";
    }
}
