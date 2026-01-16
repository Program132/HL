# HytaleLoader Documentation

Welcome to HytaleLoader - a powerful plugin loader and API wrapper for Hytale servers!

## Current Version: 1.0.1

### Latest Features (v1.0.1)
- ✅ **Item & Inventory API** - Manage player inventories
- ✅ **Complete Event System** - Player and ECS events
- ✅ **Command System** - Easy command registration
- ✅ **Full Javadoc** - Complete documentation

### Coming in 1.0.2 (In Development)
- 🚧 **GameMode API** - Get and set player game modes
- 🚧 **Enhanced Player API** - Extended gamemode management
- 🚧 **Fixs ECS Events** - Used `EntityEventSystem`

## Quick Links

- [Getting Started](getting_started.md) - Installation and first plugin
- [Events](events.md) - Event system overview
- [Standard Events](standard_events.md) - Player events reference
- [Commands](commands.md) - Command system guide
- [Server Setup](server.md) - Server configuration

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

### ECS Block Events
- `BreakBlockEvent` - When a block is broken
- `PlaceBlockEvent` - When a block is placed
- `UseBlockEvent` - When a block is used/interacted with
- `DamageBlockEvent` - When a block takes damage

### ECS Other Events
- `DropItemEvent` - When an item is dropped
- `DiscoverZoneEvent` - When a zone is discovered
- `CraftRecipeEvent` - When a recipe is crafted

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

## Version History

### 1.0.1 (Current)
- Item and Inventory API
- Player.getInventory() method
- InventoryPlayer, InventoryBlock, InventoryHotbar
- Item wrapper class
- Complete Javadoc for all event classes
- Enhanced Player API

### 1.0.0
- Initial release
- Event system with @EventHandler
- Command system with @Command
- SimplePlugin base class
- Player and ECS events
- Event dispatchers

## Installation

Add HytaleLoader as a dependency in your `pom.xml`:

```xml
<dependency>
    <groupId>fr.hytale.loader</groupId>
    <artifactId>HytaleLoader</artifactId>
    <version>1.0.2</version>
    <scope>provided</scope>
</dependency>
```

## Support & Contributing

For issues, questions, or contributions, please visit the GitHub repository.