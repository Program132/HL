package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.MouseMotionEvent;
import com.hypixel.hytale.protocol.Vector2f;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.Entity;
import fr.hytale.loader.api.Player;

/**
 * Called when a player moves their mouse.
 * <p>
 * This event is fired when a player moves their mouse cursor.
 * It provides access to the mouse motion details and interaction context.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.2
 */
public class PlayerMouseMotionEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent originalEvent;
    private final Player player;

    /**
     * Constructs a new PlayerMouseMotionEvent.
     * 
     * @param originalEvent the original Hytale player mouse motion event
     * @param player        the player who moved the mouse
     */
    public PlayerMouseMotionEvent(com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent originalEvent, Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the mouse motion event details.
     * 
     * @return the mouse motion event
     */
    public MouseMotionEvent getMouseMotion() {
        return originalEvent.getMouseMotion();
    }

    /**
     * Gets the client use time.
     * 
     * @return the client use time
     */
    public long getClientUseTime() {
        return originalEvent.getClientUseTime();
    }

    /**
     * Gets the item in the player's hand.
     * 
     * @return the item in hand
     */
    public Item getItemInHand() {
        return originalEvent.getItemInHand();
    }

    /**
     * Gets the target block position.
     * 
     * @return the target block position
     */
    public Vector3i getTargetBlock() {
        return originalEvent.getTargetBlock();
    }

    /**
     * Gets the target entity.
     * 
     * @return the target entity
     */
    public Entity getTargetEntity() {
        return originalEvent.getTargetEntity();
    }

    /**
     * Gets the screen point coordinates.
     * 
     * @return the screen point
     */
    public Vector2f getScreenPoint() {
        return originalEvent.getScreenPoint();
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
     * Gets the player who moved the mouse.
     * 
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }
}
