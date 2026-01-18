package fr.testmod;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import fr.hytale.loader.api.Item;
import fr.hytale.loader.api.Player;
import fr.hytale.loader.api.inventory.InventoryPlayer;
import fr.hytale.loader.plugin.SimplePlugin;
import fr.hytale.loader.event.EventHandler;
import fr.hytale.loader.scheduler.ScheduledTask;
import fr.hytale.loader.permission.Permission;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.BootEvent;
import java.util.List;
import java.util.logging.Level;
import fr.hytale.loader.command.CommandUtils;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.Message;
import java.io.IOException;
import fr.hytale.loader.config.Config;

import fr.hytale.loader.config.ConfigFormat;

public class TestMod extends SimplePlugin {

    public TestMod(JavaPluginInit init) {
        super(init);
    }

    @Override
    public ConfigFormat getConfigFormat() {
        return ConfigFormat.JSON;
    }

    @Override
    public void onEnable() {
        getLogger().at(Level.INFO).log("[TESTMOD] TestMod enabled!");

        if (getConfig().contains("features.welcomeMessage")) {
            getLogger().at(Level.INFO)
                    .log("[TESTMOD] FOUND Welcome message: " + getConfig().getBoolean("features.welcomeMessage"));
        } else {
            getLogger().at(Level.INFO)
                    .log("[TESTMOD] NOT FOUND Welcome message. Adding it...");
            getConfig().set("features.welcomeMessage", true);

            try {
                saveConfig();
            } catch (IOException e) {
                getLogger().at(Level.SEVERE)
                        .log("[TESTMOD] Failed to save config: " + e.getMessage());
            }
        }
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

        Permission permission = Permission.of("testmod.test");
        player.addPermission(permission);
        player.sendMessage("You have been given the permission testmod.test: " + player.hasPermission("testmod.test"));
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

        if (CommandUtils.isPlayer(ctx)) {
            Player player = CommandUtils.getPlayer(ctx);

            player.sendMessage("Has permission: " + player.hasPermission("testmod.test"));

            System.out.println("Health: " + player.getHealth());
            System.out.println("Stamina: " + player.getStamina());
            player.setHealth(20);
            player.setStamina(0);
            System.out.println("Health: " + player.getHealth());
            System.out.println("Stamina: " + player.getStamina());
        }

    }

    @fr.hytale.loader.command.Command(name = "rtp", description = "Teleports you to a random location")
    public void onRTP(com.hypixel.hytale.server.core.command.system.CommandContext ctx) {
        if (CommandUtils.isPlayer(ctx)) {
            Player player = CommandUtils.getPlayer(ctx);
            System.out.println("Current position info:");
            System.out.println(player.getPositionX());
            System.out.println(player.getPositionY());
            System.out.println(player.getPositionZ());
            System.out.println(player.getYaw());
            System.out.println(player.getPitch());
            player.teleport(10, 180, 30);
            System.out.println("Teleported to:");
            System.out.println(player.getPositionX());
            System.out.println(player.getPositionY());
            System.out.println(player.getPositionZ());
            System.out.println(player.getYaw());
            System.out.println(player.getPitch());
        }
    }

    @fr.hytale.loader.command.Command(name = "servinfo", description = "Display server information")
    public void servinfoCommand(com.hypixel.hytale.server.core.command.system.CommandContext context) {
        Player player = CommandUtils.getPlayer(context);
        if (player == null) {
            context.sendMessage(Message.raw("This command can only be used by players!"));
            return;
        }

        // Header
        player.sendMessage("═══════════════════════════════");
        player.sendMessage("         SERVER INFO");
        player.sendMessage("═══════════════════════════════");
        player.sendMessage("");

        // Players section
        int onlineCount = fr.hytale.loader.api.Server.getOnlineCount();
        player.sendMessage("Players Online: " + onlineCount);

        if (onlineCount > 0) {
            List<Player> players = fr.hytale.loader.api.Server.getOnlinePlayers();
            StringBuilder playerNames = new StringBuilder("   ");
            for (int i = 0; i < players.size(); i++) {
                playerNames.append(players.get(i).getName());
                if (i < players.size() - 1) {
                    playerNames.append(", ");
                }
            }
            player.sendMessage(playerNames.toString());
        }
        player.sendMessage("");

        // Worlds section
        List<fr.hytale.loader.api.World> worlds = fr.hytale.loader.api.Server.getWorlds();
        player.sendMessage("Loaded Worlds: " + worlds.size());

        fr.hytale.loader.api.World defaultWorld = fr.hytale.loader.api.Server.getDefaultWorld();
        String defaultWorldName = defaultWorld != null ? defaultWorld.getName() : "unknown";

        for (fr.hytale.loader.api.World world : worlds) {
            String worldName = world.getName();
            List<Player> playersInWorld = fr.hytale.loader.api.Server.getPlayersInWorld(world);
            int playerCount = playersInWorld.size();

            String isDefault = worldName.equals(defaultWorldName) ? " [DEFAULT]" : "";
            player.sendMessage(String.format("   • %s (%d players)%s",
                    worldName, playerCount, isDefault));

            // Show players in this world if any
            if (playerCount > 0 && playerCount <= 5) {
                StringBuilder worldPlayers = new StringBuilder("     Players: ");
                for (int i = 0; i < playersInWorld.size(); i++) {
                    worldPlayers.append(playersInWorld.get(i).getName());
                    if (i < playersInWorld.size() - 1) {
                        worldPlayers.append(", ");
                    }
                }
                player.sendMessage(worldPlayers.toString());
            }
        }
        player.sendMessage("═══════════════════════════════");

    }

    @fr.hytale.loader.command.Command(name = "magicwall", description = "Creates a 3x3 magic wall")
    public void onMagicWall(com.hypixel.hytale.server.core.command.system.CommandContext ctx) {
        if (!CommandUtils.isPlayer(ctx))
            return;
        Player p = CommandUtils.getPlayer(ctx);
        fr.hytale.loader.api.Location loc = p.getLocation();
        if (loc == null)
            return;

        ctx.sender().sendMessage(Message.raw("Creating magic wall..."));

        for (int x = (int) p.getPositionX(); x < p.getPositionX() + 3; x++) {
            for (int y = (int) p.getPositionY(); y < p.getPositionY() + 3; y++) {
                fr.hytale.loader.api.Location blockLoc = loc.clone();
                // Simple wall relative to player
                blockLoc.setX(blockLoc.getX() + 2 + x);
                blockLoc.setY(blockLoc.getY() + y);
                System.out.println("BLOCK PLACED: " + x + ", " + y);

                blockLoc.getWorld().setBlock(blockLoc, "Rock_Magma_Cooled");
            }
        }
        ctx.sender().sendMessage(Message.raw("Magic wall created!"));
    }
}
