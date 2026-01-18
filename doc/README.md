# HytaleLoader Documentation

Welcome to HytaleLoader - a powerful plugin loader and API wrapper for Hytale servers!

## Quick Links

- [Getting Started](getting_started.md) - Installation and first plugin
- [Events](events.md) - Event system overview
- [Standard Events](standard_events.md) - Player events reference
- [Commands](commands.md) - Command system guide
- [Server Setup](server.md) - Server configuration
- **[Server API](server_api.md)** - Server utility class
- **[Player API](player_api.md)** - Complete player management reference
- **[Block API](block_api.md)** - Block manipulation and interaction
- **[Location API](location_api.md)** - 3D position and rotation system
- **[Entity API](entity_api.md)** - Entity management and interaction
- **[World API](world_api.md)** - World wrapper and utilities
- **[Redis API](redis_api.md)** - Remote Redis database management
- **[MySQL API](mysql_api.md)** - MySQL database management
- **[Scheduler API](scheduler_api.md)** - Task scheduling and execution
- **[Permission API](permission_api.md)** - Permission management system
- **[Command Utils](command_utils.md)** - Command helper utilities
- **[Player Stats API](player_stats_api.md)** - Health, stamina, mana management
- **[Config API](config_api.md)** - YAML configuration system
- [CHANGELOG](CHANGELOG.md) - Version history

## Key Features

### 📦 **Player API** (`fr.hytale.loader.api.Player`)
Simplified wrapper for player management:
```java
Player player = event.getPlayer();

// Identity
String name = player.getName();
UUID uuid = player.getUUID();

// Messaging
player.sendMessage("Hello!");

// Location & Teleportation
Location loc = player.getLocation();
player.teleport(new Location(loc.getWorld(), 100, 64, 200));

// Inventory
Inventory inv = player.getInventory();

// Permissions
boolean hasPermission = player.hasPermission("myplugin.admin");

// Stats
float health = player.getHealth();
player.setHealth(20.0f);
```

### 🌍 **Location & World API**
```java
// Get player location
Location loc = player.getLocation();
double x = loc.getX();
double y = loc.getY();
double z = loc.getZ();
World world = loc.getWorld();

// Create and teleport
Location spawn = new Location(world, 0, 100, 0, 0, 0);
player.teleport(spawn);

// Distance calculations
double distance = loc1.distance(loc2);
```

### 📦 **Item & Inventory API**
```java
// Get player inventory
InventoryPlayer inv = new InventoryPlayer(player);

// Get items
List<Item> items = inv.getItems();
Item item = inv.getItem(slot);

// Modify inventory
inv.addItem(item);
inv.clear(); // Note: May cause issues during join event
```

### 🎪 **Event System**
```java
public class MyPlugin extends SimplePlugin {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome " + player.getName() + "!");
    }
    
    @EventHandler
    public void onBlockBreak(BreakBlockEvent event) {
        // Handle block break
        event.setCancelled(true); // Cancel the event
    }
}
```

### ⚔️ **Command System**
```java
@Command(name = "tp", description = "Teleport command")
public void teleportCommand(CommandContext context) {
    context.sendMessage("Teleporting...");
}
```

## Available Events

### Player Events
- `PlayerJoinEvent` - When a player joins
- `PlayerQuitEvent` - When a player leaves
- `PlayerChatEvent` - When a player sends a chat message
- `PlayerDamageEvent` - When a player takes damage
- `PlayerCraftEvent` - When a player crafts (deprecated)
- `PlayerMouseButtonEvent` - When a player clicked (left or right click)
- `PlayerMouseMotionEvent` - When a player moved the mouse

### ECS Block Events
- `BreakBlockEvent` - When a block is broken
- `PlaceBlockEvent` - When a block is placed
- `UseBlockEvent` - When a block is used/interacted with
- `DamageBlockEvent` - When a block takes damage

### ECS Other Events
- `DropItemEvent` - When an item is dropped
- `DiscoverZoneEvent` - When a zone is discovered
- `CraftRecipeEvent` - When a recipe is crafted
- `SwitchActiveSlotEvent` - When switching elements in inventory

## Project Structure

```
HL/
├── HytaleLoader/              # Core library
│   ├── src/main/java/fr/hytale/loader/
│   │   ├── api/               # Public API
│   │   │   ├── Player.java    # Player wrapper with stats
│   │   │   ├── GameMode.java  # GameMode enum
│   │   │   ├── Item.java      # Item wrapper
│   │   │   ├── Block.java     # Block wrapper
│   │   │   ├── World.java     # World wrapper
│   │   │   ├── Location.java  # Location wrapper
│   │   │   └── inventory/     # Inventory classes
│   │   ├── command/           # Command system
│   │   │   ├── CommandUtils.java  # Command utilities
│   │   │   └── SimpleCommand.java # Command base
│   │   │   └── CommandScanner.java # Command scannar
│   │   │   └── SimpleCommand.java # Command base
│   │   ├── event/             # Event system
│   │   │   └── types/         # Event classes (Join, Quit, Chat, etc.)
│   │   ├── internal/          # Internal dispatchers
│   │   ├── permission/        # Permission system
│   │   │   ├── Permission.java       # Permission object
│   │   │   └── PermissionManager.java # Permission storage
│   │   ├── plugin/            # Plugin base classes
│   │   │   └── SimplePlugin.java # Plugin base with Scheduler
│   │   └── scheduler/         # Task scheduling
│   │       ├── Scheduler.java      # Scheduler implementation
│   │       └── ScheduledTask.java  # Task wrapper
```

## Installation

Add HytaleLoader as a dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>fr.hytale.loader</groupId>
    <artifactId>HytaleLoader</artifactId>
    <version>1.0.5</version>
    <scope>provided</scope>
</dependency>
```

## Support & Contributing

For issues, questions, or contributions, please visit the GitHub repository.