package fr.hytale.loader.plugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import fr.hytale.loader.event.EventScanner;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.command.CommandScanner;

/**
 * Base class for HytaleLoader plugins.
 * <p>
 * This class provides a simplified API for creating Hytale server mods.
 * It automatically handles event registration, command registration, and
 * provides
 * lifecycle hooks through onEnable() and onDisable().
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.0
 */
public abstract class SimplePlugin extends JavaPlugin implements SimpleListener {

        /**
         * Constructs a new SimplePlugin instance.
         *
         * @param init the plugin initialization data provided by the Hytale server
         */
        public SimplePlugin(JavaPluginInit init) {
                super(init);
        }

        @Override
        protected void start() {
                super.start();

                // Register core event dispatcher for internal Hytale events
                fr.hytale.loader.internal.StandardEventDispatcher dispatcher = new fr.hytale.loader.internal.StandardEventDispatcher();

                // Register for native Hytale events
                getEventRegistry().registerGlobal(
                        com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent.class,
                        dispatcher::onPlayerJoin);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered AddPlayerToWorldEvent");
                getEventRegistry().registerGlobal(
                        com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent.class,
                        dispatcher::onPlayerQuit);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlayerDisconnectEvent");

                getEventRegistry().registerGlobal(
                        com.hypixel.hytale.server.core.event.events.player.PlayerCraftEvent.class,
                        dispatcher::onPlayerCraft);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlayerCraftEvent");

                getEventRegistry().registerAsyncGlobal(
                        com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent.class,
                        future -> ((java.util.concurrent.CompletableFuture<com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent>) future)
                                .thenApply(event -> {
                                        dispatcher.onPlayerChat(event);
                                        return event;
                                }));
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlayerChatEvent");

                getEventRegistry().registerGlobal(
                        com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent.class,
                        dispatcher::onPlayerMouseButton);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlayerMouseButtonEvent");

                getEventRegistry().registerGlobal(
                        com.hypixel.hytale.server.core.event.events.player.PlayerMouseMotionEvent.class,
                        dispatcher::onPlayerMouseMotion);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlayerMouseMotionEvent");

                // Register core ECS systems
                // These systems handle ECS events and dispatch them to the HytaleLoader event bus
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.DamageSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.BreakBlockSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.PlaceBlockSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.UseBlockSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.DamageBlockSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.DropItemSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.DiscoverZoneSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.CraftRecipeSystem());
                this.getEntityStoreRegistry().registerSystem(
                        (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.SwitchActiveSlotSystem());

                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered ECS Systems");

                // Auto register main class as listener and command container
                EventScanner.registerListeners(this, this);
                CommandScanner.registerCommands(this, this);
                onEnable();
        }

        @Override
        protected void shutdown() {
                onDisable();
                super.shutdown();
        }

        /**
         * Called when the plugin is enabled.
         * <p>
         * Override this method to perform initialization tasks when your plugin starts.
         * </p>
         */
        public abstract void onEnable();

        /**
         * Called when the plugin is disabled.
         * <p>
         * Override this method to perform cleanup tasks when your plugin stops.
         * </p>
         */
        public abstract void onDisable();

        public void registerListener(SimpleListener listener) {
                EventScanner.registerListeners(this, listener);
        }

        public void registerCommand(Object commandContainer) {
                CommandScanner.registerCommands(this, commandContainer);
        }
}
