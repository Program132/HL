package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.asset.type.item.config.CraftingRecipe;

/**
 * Called when a crafting recipe is executed (ECS event).
 * <p>
 * This is an ECS-level crafting event that fires during the crafting process.
 * It provides access to the recipe being crafted and the quantity.
 * The event can be cancelled to prevent the craft from completing.
 * </p>
 * 
 * <p>
 * <strong>Note:</strong> This is different from PlayerCraftEvent
 * which is a player-specific event.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.1
 */
public class CraftRecipeEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent.Pre originalEvent;

    /**
     * Constructs a new CraftRecipeEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     */
    public CraftRecipeEvent(com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent.Pre originalEvent) {
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the crafting recipe being used.
     * 
     * @return the crafting recipe
     */
    public CraftingRecipe getCraftedRecipe() {
        return originalEvent.getCraftedRecipe();
    }

    /**
     * Gets the quantity being crafted.
     * 
     * @return the number of items being crafted
     */
    public int getQuantity() {
        return originalEvent.getQuantity();
    }

    /**
     * Gets the name/ID of the recipe.
     * 
     * @return the recipe ID, or "Unknown" if not available
     */
    public String getRecipeName() {
        return getCraftedRecipe() != null ? getCraftedRecipe().getId() : "Unknown";
    }

    /**
     * Checks if this event has been cancelled.
     * 
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return originalEvent.isCancelled();
    }

    /**
     * Sets the cancelled state of this event.
     * 
     * @param cancelled true to cancel, false to allow
     */
    public void setCancelled(boolean cancelled) {
        originalEvent.setCancelled(cancelled);
    }
}
