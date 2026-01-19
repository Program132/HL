package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Player;

/**
 * Called when a block takes damage.
 * <p>
 * This event is fired when a block is being damaged but not yet broken.
 * This allows mods to track block damage progression or modify damage values.
 * The event can be cancelled to prevent damage.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.1
 */
public class DamageBlockEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent originalEvent;
    private final Player player;

    /**
     * Constructs a new DamageBlockEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     * @param player        the player who damaged the block, or null if not a
     *                      player
     */
    public DamageBlockEvent(com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent originalEvent,
            Player player) {
        this.originalEvent = originalEvent;
        this.player = player;
    }

    /**
     * Gets the item being used to damage the block.
     * 
     * @return the item in hand, or null if no item
     */
    public ItemStack getItemInHand() {
        return originalEvent.getItemInHand();
    }

    /**
     * Gets the position of the block being damaged.
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
     * Gets the type of block being damaged.
     * 
     * @return the block type
     */
    public BlockType getBlockType() {
        return originalEvent.getBlockType();
    }

    /**
     * Gets the current accumulated damage on the block.
     * 
     * @return the current damage value
     */
    public float getCurrentDamage() {
        return originalEvent.getCurrentDamage();
    }

    /**
     * Gets the damage being applied in this event.
     * 
     * @return the damage amount
     */
    public float getDamage() {
        return originalEvent.getDamage();
    }

    /**
     * Sets the damage to be applied.
     * 
     * @param damage the new damage amount
     */
    public void setDamage(float damage) {
        originalEvent.setDamage(damage);
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
     * Gets the player who damaged the block.
     * 
     * @return the player, or null if the block was not damaged by a player
     */
    public Player getPlayer() {
        return player;
    }
}
