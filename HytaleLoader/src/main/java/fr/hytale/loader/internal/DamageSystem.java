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

public class DamageSystem extends DamageEventSystem {

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store,
            CommandBuffer<EntityStore> commandBuffer, Damage event) {
        // Handle Victim is Player
        Player player = chunk.getComponent(index, Player.getComponentType());
        if (player != null) {
            PlayerRef playerRef = chunk.getComponent(index, PlayerRef.getComponentType());
            PlayerDamageEvent damageEvent = new PlayerDamageEvent(player, playerRef, event);
            HytaleServer.get().getEventBus().dispatchFor(PlayerDamageEvent.class, null).dispatch(damageEvent);
        }
    }

    @Override
    public com.hypixel.hytale.component.query.Query<EntityStore> getQuery() {
        return (com.hypixel.hytale.component.query.Query<EntityStore>) Player.getComponentType();
    }
}

