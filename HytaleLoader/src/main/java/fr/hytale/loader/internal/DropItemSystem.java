package fr.hytale.loader.internal;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

/**
 * Internal ECS system for handling item drop events.
 *
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.2
 */
public class DropItemSystem extends EntityEventSystem<EntityStore, DropItemEvent.Drop> {

    public DropItemSystem() {
        super(DropItemEvent.Drop.class);
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }

    @Override
    public void handle(
            int entityIndex,
            @Nonnull ArchetypeChunk<EntityStore> chunk,
            @Nonnull Store<EntityStore> store,
            @Nonnull CommandBuffer<EntityStore> commandBuffer,
            @Nonnull DropItemEvent.Drop event) {

        fr.hytale.loader.api.Player playerWrapper = null;

        Player nativePlayer = chunk.getComponent(entityIndex, Player.getComponentType());
        if (nativePlayer != null) {
            PlayerRef playerRef = chunk.getComponent(entityIndex, PlayerRef.getComponentType());
            if (playerRef != null) {
                playerWrapper = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
            }
        }

        fr.hytale.loader.event.types.ecs.DropItemEvent newEvent = new fr.hytale.loader.event.types.ecs.DropItemEvent(
                event, playerWrapper);

        HytaleServer.get().getEventBus()
                .dispatchFor(fr.hytale.loader.event.types.ecs.DropItemEvent.class, null)
                .dispatch(newEvent);
    }
}