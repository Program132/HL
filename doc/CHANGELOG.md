# HytaleLoader Changelog

All notable changes to this project will be documented in this file.

## [Unreleased] - Version 1.0.2 (In Development)

### Added
- `GameMode` enum with SURVIVAL and CREATIVE modes
- `Player.getGameMode()` - Get current player game mode
- `Player.setGameMode(GameMode)` - Set player game mode
- `Player.sendTitle(String)` - Send a title to the player
- `Player.sendTitleWithSubtitle(String, String)` - Send a title and subtitle to the player
- Complete Javadoc for all `inventory` classes
- Javadoc for `SimpleCommand` class
- Javadoc for `SimpleListener` interface

### Changed
- Updated `@version` tags to 1.0.2 in affected classes
- Enhanced `Player` API documentation
- Improved `PlayerQuitEvent` handling to prevent crashes during async disconnects

### Technical
- Uses `com.hypixel.hytale.protocol.GameMode` for native integration
- GameMode conversion methods: `toNative()` and `fromNative()`
- Migrated ECS event handling to dedicated `EntityEventSystem`

---

## [1.0.1] - 2026-01-15

### Added
- **Item API** (`fr.hytale.loader.api.Item`)
  - ItemStack wrapper
  - Item type and quantity management
- **Inventory API** (`fr.hytale.loader.api.inventory`)
  - `Inventory` base class
  - `InventoryPlayer` - Player inventory wrapper
  - `InventoryBlock` - Block inventory wrapper  
  - `InventoryHotbar` - Hotbar wrapper
- `Player.getInventory()` - Get player inventory
- Complete Javadoc for all event classes:
  - Player events: `PlayerJoinEvent`, `PlayerQuitEvent`, `PlayerChatEvent`, `PlayerDamageEvent`, `PlayerCraftEvent`
  - ECS events: `BreakBlockEvent`, `PlaceBlockEvent`, `UseBlockEvent`, `DamageBlockEvent`, `DropItemEvent`, `DiscoverZoneEvent`, `CraftRecipeEvent`
- Javadoc for internal classes:
  - `EcsEventDispatcher`
  - `StandardEventDispatcher`
  - `DamageSystem`

### Changed
- Package structure reorganized: `player` → `api`
- Enhanced `Player` class with inventory support

### Fixed
- Import conflicts in dispatcher classes
- Event registration issues

---

## [1.0.0] - 2026-01-14

### Added
- Initial release of HytaleLoader
- **Event System**
  - `@EventHandler` annotation
  - `SimpleListener` interface
  - Event scanning and registration
  - Player events (Join, Quit, Chat, Damage, Craft)
  - ECS events (Block events, Item events, Zone events)
- **Command System**
  - `@Command` annotation
  - Command scanning and registration
  - `SimpleCommand` wrapper
- **Plugin System**
  - `SimplePlugin` base class
  - `onEnable()` and `onDisable()` lifecycle
  - `registerListener()` and `registerCommand()` utilities
- **Player API** (Basic)
  - `getName()`, `getUUID()`
  - `sendMessage()` methods
  - `isOp()`, `hasPermission()`
  - `kick()`, `isOnline()`
- **Event Dispatchers**
  - `StandardEventDispatcher` for player events
  - `EcsEventDispatcher` for ECS events
  - `DamageSystem` for damage events
- **Scanner Utilities**
  - `EventScanner` for @EventHandler methods
  - `CommandScanner` for @Command methods

### Technical Details
- Built for Hytale Server API
- Maven project structure
- Java 25 compatible
- Wraps native Hytale event system

---

## Release Notes Format

- **Added**: New features
- **Changed**: Changes to existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Removed features
- **Fixed**: Bug fixes
- **Security**: Security fixes
- **Technical**: Implementation details

---

[Unreleased]: https://github.com/Program132/HL/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/Program132/HL/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/Program132/HL/releases/tag/v1.0.0
