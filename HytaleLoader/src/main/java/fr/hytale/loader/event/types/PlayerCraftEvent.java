package fr.hytale.loader.event.types;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;
import com.hypixel.hytale.server.core.entity.entities.Player;

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
