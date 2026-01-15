package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import fr.hytale.loader.api.Player;
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

    private final Player player;
    private final PlayerDisconnectEvent originalEvent;

    /**
     * Constructs a new PlayerQuitEvent.
     * 
     * @param player        the HytaleLoader player wrapper
     * @param originalEvent the original Hytale disconnect event
     */
    public PlayerQuitEvent(Player player, PlayerDisconnectEvent originalEvent) {
        this.player = player;
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the player who left the server.
     * 
     * @return the HytaleLoader player wrapper
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's username.
     * 
     * @return the username of the player who left
     */
    public String getPlayerName() {
        return player.getName();
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

