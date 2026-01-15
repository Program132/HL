package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
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
 * @version 1.0.2
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
     * Gets the native Hytale player reference.
     * <p>
     * The PlayerRef is used for operations that don't require the full entity to be
     * loaded,
     * such as sending messages or checking online status.
     * </p>
     *
     * @return the native player reference
     */
    public PlayerRef getNativePlayerRef() {
        return this.originalEvent.getPlayerRef();
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
