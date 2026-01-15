package fr.hytale.loader.event.types.ecs;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;

/**
 * Called when a block is used/interacted with.
 * <p>
 * This event is fired when a player right-clicks or otherwise interacts with a
 * block.
 * This can include opening doors, chests, or using interactive blocks.
 * The event can be cancelled to prevent the interaction.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.1
 */
public class UseBlockEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent.Pre originalEvent;

    /**
     * Constructs a new UseBlockEvent.
     * 
     * @param originalEvent the original Hytale ECS event
     */
    public UseBlockEvent(com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent.Pre originalEvent) {
        this.originalEvent = originalEvent;
    }

    /**
     * Gets the type of interaction being performed.
     * 
     * @return the interaction type
     */
    public InteractionType getInteractionType() {
        return originalEvent.getInteractionType();
    }

    /**
     * Gets the interaction context.
     * 
     * @return the interaction context
     */
    public InteractionContext getContext() {
        return originalEvent.getContext();
    }

    /**
     * Gets the position of the block being used.
     * 
     * @return the block position
     */
    public Vector3i getTargetBlock() {
        return originalEvent.getTargetBlock();
    }

    /**
     * Gets the type of block being used.
     * 
     * @return the block type
     */
    public BlockType getBlockType() {
        return originalEvent.getBlockType();
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
}

