# HytaleLoader

**HytaleLoader** is a Java library designed to simplify the development of mods for the Hytale server. It provides an abstraction layer inspired by the Bukkit/Spigot API, allowing developers to create mods using annotations for event handling and command registration.

## ⚠️ Disclaimer

This is an unofficial modding tool for Hytale. Hytale is in alpha and APIs may change.

## Features

-   **Annotation-based Event Handling**: Use `@EventHandler` to easily register event listeners without manually managing the `EventRegistry`.
-   **Annotation-based Command Registration**: Use `@Command` to define commands with metadata (names, aliases, permissions) directly on methods.
-   **SimplePlugin Base Class**: Extend `SimplePlugin` to automatically handle lifecycle events and component registration.
-   **Player API**: Simplified wrapper around Hytale's player system with inventory management.
-   **Item & Inventory System**: Easy-to-use API for managing inventories and items.
-   **Maven Support**: Easily integrate into your project using Maven.

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
    <version>1.0.3</version>
    <scope>provided</scope>
</dependency>
```

## Quick Start

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
        
        // Access inventory
        Inventory inv = player.getInventory();
        player.sendMessage("You have " + inv.getItems().size() + " items");
    }

    @Command(name = "hello", description = "Says hello")
    public void onHello(CommandContext ctx) {
        ctx.sender().sendMessage(Message.raw("Hello World!"));
    }
}
```

## Documentation

Comprehensive documentation is available in the `doc/` directory:

-   [📖 README](doc/README.md) - Complete documentation overview
-   [📝 Changelog](doc/CHANGELOG.md) - Version history
-   [🚀 Getting Started](doc/getting_started.md) - Installation and first plugin
-   [🎪 Event System](doc/events.md) - Event system guide
-   [👤 Player API](doc/player_api.md) - Player API reference
-   [📦 Standard Events](doc/standard_events.md) - Available events
-   [⚔️ Command System](doc/commands.md) - Command registration
-   [🎮 GameMode API](doc/gamemode_api.md) - GameMode API (v1.0.2 - In Dev)
-   [JavaDoc](https://program132.github.io/HL/) - Javadoc

## Development Workflow

If you want to compile & copy your mod, use this command (Windows):
```powershell
mvn clean install -f HytaleLoader/pom.xml; mvn clean package -f TestMod/pom.xml; Copy-Item -Force TestMod/target/TestMod-1.0.2.jar server/mods/
```

## Project Structure

```
HytaleLoader/
├── src/main/java/fr/hytale/loader/
│   ├── api/              # Public API (Player, GameMode, Item)
│   │   └── inventory/    # Inventory classes
│   ├── command/          # Command system
│   ├── event/            # Event system
│   │   └── types/        # Event classes
│   ├── internal/         # Internal dispatchers
│   └── plugin/           # Plugin base classes
├── doc/                  # Documentation
└── pom.xml              # Maven configuration
```

## Contributing

Contributions are welcome! Feel free to:
- Open issues for bugs or feature requests
- Submit pull requests
- Improve documentation
- Share your plugins made with HytaleLoader

## Version History

- **1.0.2** (current): More events (+ fixes), Player API & Gamemode API
- **1.0.1**: Item & Inventory API, Complete Javadoc
- **1.0.0**: Initial release with events and commands

See [CHANGELOG.md](doc/CHANGELOG.md) for detailed version history.


## Support

For support, questions, or to showcase your plugins:
- Open an issue on GitHub
- Check the documentation in `doc/`

---

Made with ❤️ for the Hytale modding community
