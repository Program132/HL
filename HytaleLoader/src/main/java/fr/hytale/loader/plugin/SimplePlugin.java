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
