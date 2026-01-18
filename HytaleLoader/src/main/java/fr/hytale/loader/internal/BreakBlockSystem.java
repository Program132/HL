package fr.hytale.loader.internal;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Internal ECS system for handling block break events.
 * <p>
 * This system hooks into Hytale's ECS to capture block break events along with
 * the entity that caused them.
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.2
 */
public class BreakBlockSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {

    public BreakBlockSystem() {
        super(BreakBlockEvent.class);
    }

    @Override
    public Query<EntityStore> getQuery() {
        // We want to capture events from any entity (players, etc.)
        return Query.any();
    }

    @Override
    public void handle(
            int entityIndex,
            @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull BreakBlockEvent event) {

        fr.hytale.loader.api.Player playerWrapper = null;

        // Check if the entity is a player
        Player nativePlayer = chunk.getComponent(entityIndex, Player.getComponentType());
        if (nativePlayer != null) {
            PlayerRef playerRef = chunk.getComponent(entityIndex, PlayerRef.getComponentType());
            if (playerRef != null) {
                playerWrapper = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
            }
        }

        // Create and dispatch our custom event
        fr.hytale.loader.event.types.ecs.BreakBlockEvent newEvent = new fr.hytale.loader.event.types.ecs.BreakBlockEvent(
                event, playerWrapper);

        HytaleServer.get().getEventBus()
                .dispatchFor(fr.hytale.loader.event.types.ecs.BreakBlockEvent.class, null)
                .dispatch(newEvent);
    }
}