package fr.hytale.loader.internal;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.event.types.player.PlayerJoinEvent;
import fr.hytale.loader.event.types.player.PlayerQuitEvent;
import fr.hytale.loader.event.types.player.PlayerMouseButtonEvent;
import fr.hytale.loader.event.types.player.PlayerMouseMotionEvent;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.logger.HytaleLogger;

import java.util.Objects;

/**
 * Internal dispatcher for standard player events.
 * <p>
 * This class receives native Hytale player events, wraps them in HytaleLoader
 * event objects,
 * and dispatches them through the HytaleLoader event bus for mods to listen to.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.0
 */
public class StandardEventDispatcher implements SimpleListener {

    /**
     * Handles player join events from the native Hytale event system.
     * 
     * @param event the native add player to world event
     */
    public void onPlayerJoin(AddPlayerToWorldEvent event) {
        Player nativePlayer = event.getHolder().getComponent(Player.getComponentType());
        PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());

        if (nativePlayer != null && playerRef != null) {
            // Create HytaleLoader Player wrapper
            fr.hytale.loader.api.Player player = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
            PlayerJoinEvent newEvent = new PlayerJoinEvent(player, event);
            HytaleServer.get().getEventBus().dispatchFor(PlayerJoinEvent.class, null).dispatch(newEvent);
        } else {
            HytaleLogger.getLogger().at(java.util.logging.Level.WARNING)
                    .log("Player or PlayerRef is null in AddPlayerToWorldEvent");
        }
    }

    /**
     * Handles player disconnect events from the native Hytale event system.
     * 
     * @param event the native player disconnect event
     */
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();

        Player nativePlayer = null;
        try {
            nativePlayer = playerRef.getComponent(Player.getComponentType());
        } catch (IllegalStateException e) {
            HytaleLogger.getLogger().at(java.util.logging.Level.WARNING)
                    .log("Could not get Player component during quit: " + e.getMessage());
        } catch (Exception e) {

        }

        fr.hytale.loader.api.Player player = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
        PlayerQuitEvent newEvent = new PlayerQuitEvent(player, event);
        HytaleServer.get().getEventBus().dispatchFor(PlayerQuitEvent.class, null).dispatch(newEvent);
    }

    /**
     * Handles player crafting events from the native Hytale event system.
     * 
     * @param event the native player craft event
     * @deprecated The underlying Hytale event is deprecated
     */
    @Deprecated
    @SuppressWarnings("removal")
    public void onPlayerCraft(com.hypixel.hytale.server.core.event.events.player.PlayerCraftEvent event) {
        HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] PlayerCraftEvent received!");
        try {
            fr.hytale.loader.event.types.player.PlayerCraftEvent newEvent = new fr.hytale.loader.event.types.player.PlayerCraftEvent(
                    event);
            HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                    .log("[HytaleLoader] Created PlayerCraftEvent wrapper");

            HytaleServer.get().getEventBus()
                    .dispatchFor(fr.hytale.loader.event.types.player.PlayerCraftEvent.class, null)
                    .dispatch(newEvent);
            HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Dispatched PlayerCraftEvent");
        } catch (Exception e) {
            HytaleLogger.getLogger().at(java.util.logging.Level.SEVERE)
                    .log("[HytaleLoader] Error dispatching PlayerCraftEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles player chat events from the native Hytale event system.
     * 
     * @param event the native player chat event
     */
    public void onPlayerChat(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent event) {
        HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] PlayerChatEvent received!");
        fr.hytale.loader.event.types.player.PlayerChatEvent newEvent = new fr.hytale.loader.event.types.player.PlayerChatEvent(
                event);
        HytaleServer.get().getEventBus().dispatchFor(fr.hytale.loader.event.types.player.PlayerChatEvent.class, null)
                .dispatch(newEvent);
    }

    /**
     * Handles player mouse button events from the native Hytale event system.
     * 
     * @param event the native player mouse button event
     */
    public void onPlayerMouseButton(com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent event) {
        Ref<EntityStore> plrRef = event.getPlayerRef();
        PlayerRef playerRef = plrRef.getStore().getComponent(plrRef, PlayerRef.getComponentType());

        Player nativePlayer = null;
        try {
            nativePlayer = playerRef.getComponent(Player.getComponentType());
        } catch (IllegalStateException e) {
            HytaleLogger.getLogger().at(java.util.logging.Level.WARNING)
                    .log("Could not get Player component during quit: " + e.getMessage());
        } catch (Exception e) {

        }
        fr.hytale.loader.api.Player player = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
        PlayerMouseButtonEvent newEvent = new PlayerMouseButtonEvent(event, player);
        HytaleServer.get().getEventBus().dispatchFor(PlayerMouseButtonEvent.class, null).dispatch(newEvent);
    }

    /**
     * Handles player mouse motion events from the native Hytale event system.
     * 
     * @param event the native player mouse motion event
     */
    public void onPlayerMouseMotion(com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent event) {
        Ref<EntityStore> plrRef = event.getPlayerRef();
        PlayerRef playerRef = plrRef.getStore().getComponent(plrRef, PlayerRef.getComponentType());

        Player nativePlayer = null;
        try {
            nativePlayer = playerRef.getComponent(Player.getComponentType());
        } catch (IllegalStateException e) {
            HytaleLogger.getLogger().at(java.util.logging.Level.WARNING)
                    .log("Could not get Player component during quit: " + e.getMessage());
        } catch (Exception e) {

        }
        fr.hytale.loader.api.Player player = new fr.hytale.loader.api.Player(nativePlayer, playerRef);
        PlayerMouseMotionEvent newEvent = new PlayerMouseMotionEvent(event, player);
        HytaleServer.get().getEventBus().dispatchFor(PlayerMouseMotionEvent.class, null).dispatch(newEvent);
    }
}
