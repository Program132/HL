# HytaleLoader

**HytaleLoader** is a Java library designed to simplify the development of mods for the Hytale server. It provides an abstraction layer inspired by the Bukkit/Spigot API, allowing developers to create mods using annotations for event handling and command registration and much more!

## ⚠️ Disclaimer

This is an unofficial modding tool for Hytale. Hytale is in alpha and APIs may change.

## Features

### Core Systems
- **Annotation-based Event Handling**: Use `@EventHandler` to easily register event listeners
- **Annotation-based Command Registration**: Use `@Command` to define commands with metadata
- **SimplePlugin Base Class**: Automatic lifecycle management and component registration
- **Configuration System**: YAML and JSON config support with automatic loading

### Player & World APIs
- **Player API**: Comprehensive player management with inventory, permissions, and stats
- **Entity API** Base entity class with location, teleportation, and removal
- **World API** World wrapper with block manipulation and entity spawning
- **Block API** Block class
- **Location API**: 3D position and rotation handling
- **Player Stats API**: Health, stamina, oxygen, mana management (thread-safe)

### Data Storage 
- **Redis API**: Connection pooling, String/Hash/List/Set operations
- **MySQL API**: Connection pooling, prepared statements, transactions, batch operations

### Advanced Features
- **Scheduler System**: Execute tasks immediately, delayed, or periodically (sync and async)
- **Permission System**: Hierarchical permission management with wildcards
- **Command Utilities**: Helper methods for command development
- **Item & Inventory System**: Easy-to-use API for managing inventories and items
- **Maven Support**: Easily integrate into your project

## Installation

### Building locally

```bash
git clone https://github.com/Program132/HL.git
cd HytaleLoader
mvn clean install
```

### Adding dependency

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>fr.hytale.loader</groupId>
    <artifactId>HytaleLoader</artifactId>
    <version>1.0.5</version>
    <scope>provided</scope>
</dependency>
```

## Quick Start

### Basic Plugin

```java
public class MyMod extends SimplePlugin {

    public MyMod(JavaPluginInit init) {
        super(init);
    }

    @Override
    public void onEnable() {
        getLogger().info("MyMod enabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("Welcome " + player.getName() + "!");
        
        // Get player location
        Location loc = player.getLocation();
        player.sendMessage("You are at: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
    }

    @Command(name = "hello", description = "Says hello")
    public void onHello(CommandContext ctx) {
        ctx.sender().sendMessage(Message.raw("Hello World!"));
    }
}
```

## Documentation

Comprehensive documentation is available in the `doc/` directory:

### Getting Started
- [📖 README](doc/README.md) - Complete documentation overview
- [🚀 Getting Started](doc/getting_started.md) - Installation and first plugin
- [📝 Changelog](doc/CHANGELOG.md) - Version history

### Core APIs
- [👤 Player API](doc/player_api.md) - Player management
- [🌍 World API](doc/world_api.md) - World manipulation
- [🧱 Block API](doc/block_api.md) - Block operations
- [📍 Location API](doc/location_api.md) - Position and rotation
- [👾 Entity API](doc/entity_api.md) - Entity management
- [📊 Player Stats API](doc/player_stats_api.md) - Health, stamina, mana

### Data Storage
- [🔴 Redis API](doc/redis_api.md) - Redis database
- [🐬 MySQL API](doc/mysql_api.md) - MySQL database

### Systems
- [🎪 Event System](doc/events.md) - Event handling
- [⚔️ Command System](doc/commands.md) - Command registration
- [🕐 Scheduler API](doc/scheduler_api.md) - Task scheduling
- [🔐 Permission API](doc/permission_api.md) - Permissions
- [🛠️ Command Utils](doc/command_utils.md) - Command helpers
- [📝 Config API](doc/config_api.md) - Configuration system

### Reference
- [📦 Standard Events](doc/standard_events.md) - Available events
- [JavaDoc](https://program132.github.io/HL/) - Full API reference

## Development Workflow

To compile and deploy your mod (Windows):
```powershell
mvn clean install -f HytaleLoader/pom.xml; mvn clean package -f TestMod/pom.xml; Copy-Item -Force TestMod/target/TestMod-1.0.5.jar server/mods/
```

## Project Structure

```
HL/
├── HytaleLoader/              # Core library
│   ├── src/main/java/fr/hytale/loader/
│   │   ├── api/               # Public API
│   │   │   ├── Player.java    # Player wrapper
│   │   │   ├── Entity.java    # Entity wrapper
│   │   │   ├── World.java     # World wrapper
│   │   │   ├── Block.java     # Block wrapper
│   │   │   ├── Location.java  # Location wrapper
│   │   │   └── inventory/     # Inventory classes
│   │   ├── datastorage/       # Data storage
│   │   │   ├── RedisClient.java   # Redis client
│   │   │   └── MySQLClient.java   # MySQL client
│   │   ├── command/           # Command system
│   │   ├── event/             # Event system
│   │   ├── config/            # Configuration system
│   │   ├── permission/        # Permission system
│   │   ├── scheduler/         # Task scheduling
│   │   └── plugin/            # Plugin base classes
└── TestMod/                   # Example mod
```

## Contributing

Contributions are welcome! Feel free to:
- Open issues for bugs or feature requests
- Submit pull requests
- Improve documentation
- Share your plugins made with HytaleLoader

See [CHANGELOG.md](doc/CHANGELOG.md) for detailed version history.

## Support

For support, questions, or to showcase your plugins:
- Open an issue on GitHub
- Check the documentation in `doc/`

---

Made with ❤️ for the Hytale modding community
