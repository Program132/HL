package fr.hytale.loader.plugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import fr.hytale.loader.event.EventScanner;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.command.CommandScanner;

public abstract class SimplePlugin extends JavaPlugin implements SimpleListener {

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

    public abstract void onEnable();

    public abstract void onDisable();

    public void registerListener(SimpleListener listener) {
        EventScanner.registerListeners(this, listener);
    }

    public void registerCommand(Object commandContainer) {
        CommandScanner.registerCommands(this, commandContainer);
    }
}
