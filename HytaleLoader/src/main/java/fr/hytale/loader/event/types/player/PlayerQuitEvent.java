package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

/**
 * Called when a player leaves the server.
 * <p>
 * This event is fired when a player disconnects from the server, whether by
 * logging out normally or due to a connection loss.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.0
 */
public class PlayerQuitEvent implements IEvent<Void> {

    private final PlayerDisconnectEvent originalEvent;

    /**
     * Constructs a new PlayerQuitEvent.
     * 
     * @param originalEvent the original Hytale disconnect event
     */
    public PlayerQuitEvent(PlayerDisconnectEvent originalEvent) {
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the player who left the server.
     * 
     * @return the player entity
     */
    public Player getPlayer() {
        return originalEvent.getPlayerRef().getComponent(Player.getComponentType());
    }

    /**
     * Gets the player's username.
     * 
     * @return the username of the player who left
     */
    public String getPlayerName() {
        return originalEvent.getPlayerRef().getUsername();
    }

    /**
     * Gets the original Hytale event.
     * 
     * @return the wrapped PlayerDisconnectEvent
     */
    public PlayerDisconnectEvent getOriginalEvent() {
        return originalEvent;
    }
}
