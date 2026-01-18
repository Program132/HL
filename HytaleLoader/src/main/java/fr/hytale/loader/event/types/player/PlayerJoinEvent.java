package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import fr.hytale.loader.api.Player;
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
 * @version 1.0.5
 * @since 1.0.0
 */
public class PlayerJoinEvent implements IEvent<Void> {

    private final Player player;
    private final AddPlayerToWorldEvent originalEvent;

    /**
     * Constructs a new PlayerJoinEvent.
     * 
     * @param player        the HytaleLoader player wrapper
     * @param originalEvent the original Hytale event
     */
    public PlayerJoinEvent(Player player, AddPlayerToWorldEvent originalEvent) {
        this.player = player;
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the player who joined.
     * 
     * @return the HytaleLoader player wrapper
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
        return player.getPlayerRef();
    }

    /**
     * Gets the player's username.
     * 
     * @return the player's username
     */
    public String getPlayerName() {
        return player.getName();
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
