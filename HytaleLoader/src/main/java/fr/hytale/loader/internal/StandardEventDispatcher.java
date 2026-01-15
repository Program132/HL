package fr.hytale.loader.internal;

import fr.hytale.loader.event.EventHandler;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.event.types.player.PlayerJoinEvent;
import fr.hytale.loader.event.types.player.PlayerQuitEvent;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.logger.HytaleLogger;

public class StandardEventDispatcher implements SimpleListener {

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

    public void onPlayerQuit(PlayerDisconnectEvent event) {
        PlayerQuitEvent newEvent = new PlayerQuitEvent(event);
        HytaleServer.get().getEventBus().dispatchFor(PlayerQuitEvent.class, null).dispatch(newEvent);
    }

    public void onPlayerCraft(com.hypixel.hytale.server.core.event.events.player.PlayerCraftEvent event) {
        HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] PlayerCraftEvent received!");
        try {
            fr.hytale.loader.event.types.player.PlayerCraftEvent newEvent = new fr.hytale.loader.event.types.player.PlayerCraftEvent(
                    event);
            HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                    .log("[HytaleLoader] Created PlayerCraftEvent wrapper");

            HytaleServer.get().getEventBus().dispatchFor(fr.hytale.loader.event.types.player.PlayerCraftEvent.class, null)
                    .dispatch(newEvent);
            HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Dispatched PlayerCraftEvent");
        } catch (Exception e) {
            HytaleLogger.getLogger().at(java.util.logging.Level.SEVERE)
                    .log("[HytaleLoader] Error dispatching PlayerCraftEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onPlayerChat(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent event) {
        HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] PlayerChatEvent received!");
        fr.hytale.loader.event.types.player.PlayerChatEvent newEvent = new fr.hytale.loader.event.types.player.PlayerChatEvent(event);
        HytaleServer.get().getEventBus().dispatchFor(fr.hytale.loader.event.types.player.PlayerChatEvent.class, null)
                .dispatch(newEvent);
    }
}

