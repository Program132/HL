package fr.testmod;

import fr.hytale.loader.plugin.SimplePlugin;
import fr.hytale.loader.event.EventHandler;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import java.util.logging.Level;

public class TestMod extends SimplePlugin {

    public TestMod(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void onEnable() {
        getLogger().at(Level.INFO).log("[TESTMOD] TestMod enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().at(Level.INFO).log("[TESTMOD] TestMod disabled!");
    }

    @EventHandler
    public void onBoot(BootEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Server has booted!");
    }

    @EventHandler
    public void onJoin(fr.hytale.loader.event.types.PlayerJoinEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Join: " + event.getPlayerName());
    }

    @EventHandler
    public void onQuit(fr.hytale.loader.event.types.PlayerQuitEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Quit: " + event.getPlayerName());
    }

    @EventHandler
    public void onDamage(fr.hytale.loader.event.types.PlayerDamageEvent event) {
        getLogger().at(Level.INFO)
                .log("[TESTMOD] Damage: " + event.getPlayerName() + " took " + event.getDamage().getAmount());
    }

    @EventHandler
    public void onChat(fr.hytale.loader.event.types.PlayerChatEvent event) {
        getLogger().at(Level.INFO)
                .log("[TESTMOD] Chat: " + event.getSender().getUsername() + " says: " + event.getMessage());
    }

    @EventHandler
    public void onCraft(fr.hytale.loader.event.types.PlayerCraftEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Craft: " + event.getPlayerName()
                + " crafted " + event.getQuantity() + "x " + event.getRecipeName());
    }

    @fr.hytale.loader.command.Command(name = "hello", description = "Says hello")
    public void onHello(com.hypixel.hytale.server.core.command.system.CommandContext ctx) {
        ctx.sender().sendMessage(com.hypixel.hytale.server.core.Message.raw("Hello World!"));
    }
}
