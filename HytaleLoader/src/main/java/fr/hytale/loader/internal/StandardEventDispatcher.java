package fr.hytale.loader.internal;

import fr.hytale.loader.event.EventHandler;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.event.types.PlayerJoinEvent;
import fr.hytale.loader.event.types.PlayerQuitEvent;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.logger.HytaleLogger;

public class StandardEventDispatcher implements SimpleListener {

    @EventHandler
    public void onPlayerJoin(AddPlayerToWorldEvent event) {
        if (event.getHolder() == null)
            return;
        Player player = (Player) event.getHolder().getComponent(Player.getComponentType());
        PlayerRef playerRef = (PlayerRef) event.getHolder().getComponent(PlayerRef.getComponentType());

        if (player != null && playerRef != null) {
            PlayerJoinEvent newEvent = new PlayerJoinEvent(player, playerRef, event);
            HytaleServer.get().getEventBus().dispatchFor(PlayerJoinEvent.class, null).dispatch(newEvent);
        } else {
            HytaleLogger.getLogger().at(java.util.logging.Level.WARNING)
                    .log("Player or PlayerRef is null in AddPlayerToWorldEvent");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        PlayerQuitEvent newEvent = new PlayerQuitEvent(event);
        HytaleServer.get().getEventBus().dispatchFor(PlayerQuitEvent.class, null).dispatch(newEvent);
    }
}
