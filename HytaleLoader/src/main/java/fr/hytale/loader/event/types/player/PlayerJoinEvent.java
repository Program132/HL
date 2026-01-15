package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;

/**
 * Called when a player joins the server.
 * <p>
 * This event is fired after the player has successfully connected to the server
 * and their data has been loaded. It provides access to the player entity and
 * allows customization of the join message.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.0
 */
public class PlayerJoinEvent implements IEvent<Void> {

    private final Player player;
    private final PlayerRef playerRef;
    private final AddPlayerToWorldEvent originalEvent;

    /**
     * Constructs a new PlayerJoinEvent.
     * 
     * @param player        the player entity
     * @param playerRef     the player reference
     * @param originalEvent the original Hytale event
     */
    public PlayerJoinEvent(Player player, PlayerRef playerRef, AddPlayerToWorldEvent originalEvent) {
        this.player = player;
        this.playerRef = playerRef;
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the player entity.
     * 
     * @return the player who joined
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player reference.
     * 
     * @return the player reference
     */
    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    /**
     * Gets the player's username.
     * 
     * @return the player's username
     */
    public String getPlayerName() {
        return playerRef.getUsername();
    }

    /**
     * Gets the original Hytale event.
     * 
     * @return the wrapped AddPlayerToWorldEvent
     */
    public AddPlayerToWorldEvent getOriginalEvent() {
        return originalEvent;
    }

    /**
     * Sets whether the join message should be broadcast to all players.
     * 
     * @param broadcast {@code true} to broadcast the join message, {@code false} to
     *                  hide it
     */
    public void setJoinMessage(boolean broadcast) {
        originalEvent.setBroadcastJoinMessage(broadcast);
    }
}
