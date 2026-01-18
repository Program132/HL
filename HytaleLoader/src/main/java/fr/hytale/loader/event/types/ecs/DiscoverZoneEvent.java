package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import fr.hytale.loader.api.Player;

/**
 * Called when a player discovers a new zone.
 * <p>
 * This event is fired when a player enters a previously undiscovered zone
 * or area in the game world. This can be used to trigger custom messages,
 * rewards, or other zone-discovery mechanics.
 * The event can be cancelled to prevent the discovery notification.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.1
 */
public class DiscoverZoneEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent.Display originalEvent;
    private final Player player;

    /**
     * Constructs a new DiscoverZoneEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who discovered the zone
     */
    public DiscoverZoneEvent(com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent.Display originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the zone discovery information.
     * 
     * @return the discovery info containing zone details
     */
    public WorldMapTracker.ZoneDiscoveryInfo getDiscoveryInfo() {
        return originalEvent.getDiscoveryInfo();
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
     * Gets the player who discovered the zone.
     * 
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
