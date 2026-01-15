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
 * It automatically handles event registration, command registration, and provides
 * lifecycle hooks through onEnable() and onDisable().
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
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

                // Register ECS event dispatcher
                fr.hytale.loader.internal.EcsEventDispatcher ecsDispatcher = new fr.hytale.loader.internal.EcsEventDispatcher();

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent.class,
                                ecsDispatcher::onBreakBlock);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered BreakBlockEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent.class,
                                ecsDispatcher::onPlaceBlock);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered PlaceBlockEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent.Pre.class,
                                ecsDispatcher::onUseBlock);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered UseBlockEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent.class,
                                ecsDispatcher::onDamageBlock);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered DamageBlockEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent.Drop.class,
                                ecsDispatcher::onDropItem);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered DropItemEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent.Display.class,
                                ecsDispatcher::onDiscoverZone);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered DiscoverZoneEvent");

                getEventRegistry().registerGlobal(
                                com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent.Pre.class,
                                ecsDispatcher::onCraftRecipe);
                getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] Registered CraftRecipeEvent");

                // Register core ECS systems
                this.getEntityStoreRegistry().registerSystem(
                                (com.hypixel.hytale.component.system.ISystem) new fr.hytale.loader.internal.DamageSystem());

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


