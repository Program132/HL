package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import fr.hytale.loader.api.Player;

/**
 * Called when a player switches their active hotbar slot.
 * <p>
 * This event is fired when a player changes their selected slot in the hotbar.
 * It provides access to the new slot index, the previous slot index, and other
 * details.
 * The event can be cancelled to prevent the slot switch.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.2
 */
public class SwitchActiveSlotEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.SwitchActiveSlotEvent originalEvent;
    private final Player player;

    /**
     * Constructs a new SwitchActiveSlotEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who switched slots, or null if not a player
     */
    public SwitchActiveSlotEvent(com.hypixel.hytale.server.core.event.events.ecs.SwitchActiveSlotEvent originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the new active slot index.
     * 
     * @return the new slot index
     */
    public byte getNewSlot() {
        return originalEvent.getNewSlot();
    }

    /**
     * Sets the new active slot index.
     * 
     * @param newSlot the new slot index
     */
    public void setNewSlot(byte newSlot) {
        originalEvent.setNewSlot(newSlot);
    }

    /**
     * Gets the previous active slot index.
     * 
     * @return the previous slot index
     */
    public int getPreviousSlot() {
        return originalEvent.getPreviousSlot();
    }

    /**
     * Gets the inventory section ID.
     * 
     * @return the inventory section ID
     */
    public int getInventorySectionId() {
        return originalEvent.getInventorySectionId();
    }

    /**
     * Checks if the switch was requested by the server.
     * 
     * @return true if requested by the server, false otherwise
     */
    public boolean isServerRequest() {
        return originalEvent.isServerRequest();
    }

    /**
     * Checks if the switch was requested by the client.
     * 
     * @return true if requested by the client, false otherwise
     */
    public boolean isClientRequest() {
        return originalEvent.isClientRequest();
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
     * Gets the player who switched slots.
     * 
     * @return the player, or null if the action was not performed by a player
     */
    public Player getPlayer() {
        return player;
    }
}
