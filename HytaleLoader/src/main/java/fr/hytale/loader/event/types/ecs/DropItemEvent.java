package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Player;

/**
 * Called when an item is dropped.
 * <p>
 * This event is fired when a player drops an item from their inventory.
 * It provides access to the item stack and throw speed.
 * The event can be cancelled to prevent the item from being dropped.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.1
 */
public class DropItemEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent.Drop originalEvent;
    private final Player player;

    /**
     * Constructs a new DropItemEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who dropped the item, or null if not a player
     */
    public DropItemEvent(com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent.Drop originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the item stack being dropped.
     * 
     * @return the item stack
     */
    public ItemStack getItemStack() {
        return originalEvent.getItemStack();
    }

    /**
     * Sets the item stack to be dropped.
     * 
     * @param itemStack the new item stack
     */
    public void setItemStack(ItemStack itemStack) {
        originalEvent.setItemStack(itemStack);
    }

    /**
     * Gets the velocity at which the item will be thrown.
     * 
     * @return the throw speed
     */
    public float getThrowSpeed() {
        return originalEvent.getThrowSpeed();
    }

    /**
     * Sets the velocity at which the item will be thrown.
     * 
     * @param throwSpeed the new throw speed
     */
    public void setThrowSpeed(float throwSpeed) {
        originalEvent.setThrowSpeed(throwSpeed);
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

    /**
     * Gets the player who dropped the item.
     * 
     * @return the player, or null if the item was not dropped by a player
     */
    public Player getPlayer() {
        return player;
    }
}
