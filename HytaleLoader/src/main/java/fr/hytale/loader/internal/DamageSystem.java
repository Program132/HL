package fr.hytale.loader.internal;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.hytale.loader.event.types.player.PlayerDamageEvent;
import com.hypixel.hytale.component.Store;

import com.hypixel.hytale.server.core.universe.PlayerRef;

/**
 * Internal ECS system for handling player damage events.
 * <p>
 * This system hooks into Hytale's damage event processing to detect when
 * players
 * take damage and dispatches HytaleLoader's PlayerDamageEvent for mods to
 * listen to.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.0
 */
public class DamageSystem extends DamageEventSystem {

    /**
     * Handles damage events from the ECS and dispatches PlayerDamageEvent.
     * 
     * @param index         the index in the archetype chunk
     * @param chunk         the archetype chunk containing entity data
     * @param store         the entity store
     * @param commandBuffer the command buffer for entity operations
     * @param event         the damage event
     */
    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store,
            CommandBuffer<EntityStore> commandBuffer, Damage event) {
        // Handle Victim is Player
        Player nativePlayer = chunk.getComponent(index, Player.getComponentType());
        if (nativePlayer != null) {
            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            if (playerRef != null) {
                // Create HytaleLoader Player wrapper
                fr.hytale.loader.api.Player player = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
                PlayerDamageEvent damageEvent = new PlayerDamageEvent(player, event);
                HytaleServer.get().getEventBus().dispatchFor(PlayerDamageEvent.class, null).dispatch(damageEvent);
            }
        }
    }

    /**
     * Gets the query for this system to determine which entities it processes.
     * 
     * @return the component query for player entities
     */
    @Override
    public com.hypixel.hytale.component.query.Query<EntityStore> getQuery() {
        return (com.hypixel.hytale.component.query.Query<EntityStore>) Player.getComponentType();
    }
}
