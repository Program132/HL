package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Player;

/**
 * Called when a block is broken.
 * <p>
 * This event is fired when any entity (including players) breaks a block in the
 * world.
 * It provides information about the block type, position, and the item used.
 * The event can be cancelled to prevent the block from being broken.
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.1
 */
public class BreakBlockEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent originalEvent;
    private final Player player;

    /**
     * Constructs a new BreakBlockEvent.
     *
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who broke the block, or null if not a player
     */
    public BreakBlockEvent(com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the item being used to break the block.
     *
     * @return the item in hand, or null if no item
     */
    public ItemStack getItemInHand() {
        return originalEvent.getItemInHand();
    }

    /**
     * Gets the position of the block being broken.
     *
     * @return the block position
     */
    public Vector3i getTargetBlock() {
        return originalEvent.getTargetBlock();
    }

    /**
     * Gets the type of block being broken.
     *
     * @return the block type
     */
    public BlockType getBlockType() {
        return originalEvent.getBlockType();
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
     * Gets the player who broke the block.
     *
     * @return the player, or null if the block was not broken by a player
     */
    public Player getPlayer() {
        return player;
    }
}