package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Player;

/**
 * Called when a block is placed.
 * <p>
 * This event is fired when any entity (including players) places a block in the
 * world.
 * It provides information about the position, rotation, and the item used.
 * The event can be cancelled to prevent the block from being placed.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.1
 */
public class PlaceBlockEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent originalEvent;
    private final Player player;

    /**
     * Constructs a new PlaceBlockEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who placed the block, or null if not a player
     */
    public PlaceBlockEvent(com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the item being used to place the block.
     * 
     * @return the item in hand, or null if no item
     */
    public ItemStack getItemInHand() {
        return originalEvent.getItemInHand();
    }

    /**
     * Gets the position where the block is being placed.
     * 
     * @return the block position
     */
    public Vector3i getTargetBlock() {
        return originalEvent.getTargetBlock();
    }

    /**
     * Sets the target block position.
     * 
     * @param targetBlock the new target position
     */
    public void setTargetBlock(Vector3i targetBlock) {
        originalEvent.setTargetBlock(targetBlock);
    }

    /**
     * Gets the rotation of the block being placed.
     * 
     * @return the block rotation
     */
    public RotationTuple getRotation() {
        return originalEvent.getRotation();
    }

    /**
     * Sets the rotation of the block.
     * 
     * @param rotation the new rotation
     */
    public void setRotation(RotationTuple rotation) {
        originalEvent.setRotation(rotation);
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
     * Gets the player who placed the block.
     * 
     * @return the player, or null if the block was not placed by a player
     */
    public Player getPlayer() {
        return player;
    }
}
