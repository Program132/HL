package fr.testmod;

import fr.hytale.loader.api.Item;
import fr.hytale.loader.api.Player;
import fr.hytale.loader.api.inventory.InventoryPlayer;
import fr.hytale.loader.plugin.SimplePlugin;
import fr.hytale.loader.event.EventHandler;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.BootEvent;

import java.util.List;
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
    public void onJoin(fr.hytale.loader.event.types.player.PlayerJoinEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Join: " + event.getPlayerName());

         Player player = event.getPlayer();
         InventoryPlayer inv = new InventoryPlayer(player);
         if (inv != null) {
             inv.clear();
             getLogger().at(Level.INFO).log("[TESTMOD] Cleared inventory for " +
                     event.getPlayerName());
         }
    }

    @EventHandler
    public void onQuit(fr.hytale.loader.event.types.player.PlayerQuitEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Quit: " + event.getPlayerName());
    }

    @EventHandler
    public void onDamage(fr.hytale.loader.event.types.player.PlayerDamageEvent event) {
        getLogger().at(Level.INFO)
                .log("[TESTMOD] Damage: " + event.getPlayerName() + " took " + event.getDamage().getAmount());
    }

    @EventHandler
    public void onChat(fr.hytale.loader.event.types.player.PlayerChatEvent event) {
        getLogger().at(Level.INFO)
                .log("[TESTMOD] Chat: " + event.getSender().getUsername() + " says: " + event.getMessage());
    }

    @EventHandler
    public void onCraft(fr.hytale.loader.event.types.player.PlayerCraftEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] Craft: " + event.getPlayerName()
                + " crafted " + event.getQuantity() + "x " + event.getRecipeName());
    }

    // ECS Events
    @EventHandler
    public void onBreakBlock(fr.hytale.loader.event.types.ecs.BreakBlockEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] BreakBlock: " + event.getBlockType().getId()
                + " at " + event.getTargetBlock());
    }

    @EventHandler
    public void onPlaceBlock(fr.hytale.loader.event.types.ecs.PlaceBlockEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] PlaceBlock: block at " + event.getTargetBlock());
    }

    @EventHandler
    public void onUseBlock(fr.hytale.loader.event.types.ecs.UseBlockEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] UseBlock: " + event.getBlockType().getId()
                + " at " + event.getTargetBlock() + " (interaction: " + event.getInteractionType() + ")");
    }

    @EventHandler
    public void onDamageBlock(fr.hytale.loader.event.types.ecs.DamageBlockEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] DamageBlock: " + event.getBlockType().getId()
                + " damage: " + event.getDamage() + " (current: " + event.getCurrentDamage() + ")");
    }

    @EventHandler
    public void onDropItem(fr.hytale.loader.event.types.ecs.DropItemEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] DropItem: " + event.getItemStack().getItem().getId()
                + " x" + event.getItemStack().getQuantity());
    }

    @EventHandler
    public void onDiscoverZone(fr.hytale.loader.event.types.ecs.DiscoverZoneEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] DiscoverZone: " + event.getDiscoveryInfo());
    }

    @EventHandler
    public void onCraftRecipe(fr.hytale.loader.event.types.ecs.CraftRecipeEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] CraftRecipe: " + event.getRecipeName()
                + " x" + event.getQuantity());
    }

    @fr.hytale.loader.command.Command(name = "hello", description = "Says hello")
    public void onHello(com.hypixel.hytale.server.core.command.system.CommandContext ctx) {
        ctx.sender().sendMessage(com.hypixel.hytale.server.core.Message.raw("Hello World!"));
    }
}
