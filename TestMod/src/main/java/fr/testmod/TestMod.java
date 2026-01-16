package fr.testmod;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Item;
import fr.hytale.loader.api.Player;
import fr.hytale.loader.api.inventory.InventoryPlayer;
import fr.hytale.loader.plugin.SimplePlugin;
import fr.hytale.loader.event.EventHandler;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import fr.hytale.loader.scheduler.ScheduledTask;

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

            Item item = new Item(new ItemStack("Weapon_Sword_Cobalt"));
            inv.setItem(item, 5);
            inv.setItem(item, 13);
            inv.addItem(item);
        }

        getScheduler().runTask(() -> {
            player.sendMessage("Welcome!");
        });

        getScheduler().runTaskLater(() -> {
            player.sendMessage("5 seconds passed!");
        }, 5000);

        ScheduledTask task = getScheduler().runTaskTimer(() -> {
            player.sendMessage("Tick!");
        }, 0, 1000); // every second

        getScheduler().runTaskLater(task::cancel, 10000);
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

        Player p = event.getPlayer();
        p.sendTitle("Welcome to Hytale");
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

    @EventHandler
    public void onSwitchActiveSlot(fr.hytale.loader.event.types.ecs.SwitchActiveSlotEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] SwitchActiveSlot: " + event.getPreviousSlot()
                + " -> " + event.getNewSlot() + " (server: " + event.isServerRequest() + ")");
    }

    @EventHandler
    public void onPlayerMouseButton(fr.hytale.loader.event.types.player.PlayerMouseButtonEvent event) {
        getLogger().at(Level.INFO).log("[TESTMOD] MouseButton: "
                + " button: " + event.getMouseButton().mouseButtonType + " player: " + event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerMouseMotion(fr.hytale.loader.event.types.player.PlayerMouseMotionEvent event) {
        getLogger().at(Level.INFO)
                .log("[TESTMOD] MouseMotion: x=" + event.getScreenPoint().x + " y=" + event.getScreenPoint().y);
    }

    @fr.hytale.loader.command.Command(name = "hello", description = "Says hello")
    public void onHello(com.hypixel.hytale.server.core.command.system.CommandContext ctx) {
        ctx.sender().sendMessage(com.hypixel.hytale.server.core.Message.raw("Hello World!"));
    }
}
