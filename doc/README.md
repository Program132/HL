# HytaleLoader Documentation

Welcome to HytaleLoader - a powerful plugin loader and API wrapper for Hytale servers!

## Quick Links

- [Getting Started](getting_started.md) - Installation and first plugin
- [Events](events.md) - Event system overview
- [Standard Events](standard_events.md) - Player events reference
- [Commands](commands.md) - Command system guide
- [Server Setup](server.md) - Server configuration
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

// Inventory
Inventory inv = player.getInventory();

// Permissions
boolean hasPermission = player.hasPermission("myplugin.admin");
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
HytaleLoader/
├── src/main/java/fr/hytale/loader/
│   ├── api/              # Public API (Player, Item)
│   │   └── inventory/    # Inventory classes
│   ├── command/          # Command system
│   ├── event/            # Event system
│   │   └── types/        # Event classes
│   ├── internal/         # Internal dispatchers
│   └── plugin/           # Plugin base classes
```

## Installation

Add HytaleLoader as a dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>fr.hytale.loader</groupId>
    <artifactId>HytaleLoader</artifactId>
    <version>1.0.3</version>
    <scope>provided</scope>
</dependency>
```

## Support & Contributing

For issues, questions, or contributions, please visit the GitHub repository.