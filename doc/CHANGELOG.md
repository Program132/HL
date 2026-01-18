# HytaleLoader Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- **Entity API**: New `fr.hytale.loader.api.Entity` class as a base for all entities.
  - Supports `getID()`, `getUUID()`, `getLocation()`, `teleport()`, `remove()`.
- **World Entity Retrieval**: Added `world.getEntity(int id)` and `world.getEntity(UUID uuid)`.
- **Player Inheritance**: `Player` now extends `Entity`, inheriting unified methods.
- Added `World.spawnEntity(Location, String) method.`
- **Block API**
  - `world.setBlock(x, y, z, id)` - Set block at coordinates
  - `world.setBlock(Location, id)` - Set block at location
  - `world.setBlock(Block block)` - Set block at location
  - `world.getBlockIdentifier(x, y, z)` - Get block ID string
  - `world.getBlockIdentifier(Location)` - Get block ID from location
  - **`Block`** class - OOP wrapper for blocks
    - `world.getBlockAt(x, y, z)` - Get Block object
    - `block.getType()` - Get identifier
    - `block.setType(id)` - Set block type
    - `block.getLocation()` - Get location
---

## [1.0.4] - 2026-01-18

### Added

#### Server API
- **`Server`** utility class - Static server operations
  - `getOnlinePlayers()` - Get list of online players
  - `getPlayer(UUID/String)` - Find player by ID or name
  - `broadcast(String)` - Send global message
  - `broadcastPermission(String, String)` - Send message to permission group
  - `getWorlds()` - Get all loaded worlds
  - `getWorld(String)` - Get world by name
  - `getDefaultWorld()` - Get default world
  - `getOnlineCount()` - Get player count
  - `getPlayersInWorld(World)` - Get players in specific world

#### 📝 Config System
- **JSON Support** - New JSON configuration format
  - `ConfigFormat` enum - YAML and JSON support
  - `SimplePlugin.getConfigFormat()` - Override to choose format
  - `JsonConfig` implementation using Gson
  - `BaseConfig` abstract class for shared logic

#### Location & World API
- **`Location`** class - 3D position with rotation and world
  - `Location(World, x, y, z)` - Position-only constructor
  - `Location(World, x, y, z, yaw, pitch)` - Full constructor with rotation
  - `getX()`, `getY()`, `getZ()` - Get position coordinates
  - `getYaw()`, `getPitch()` - Get rotation angles
  - `getWorld()` - Get world reference
  - `setX()`, `setY()`, `setZ()` - Set position coordinates
  - `setYaw()`, `setPitch()` - Set rotation angles
  - `distance(Location)` - Calculate distance between locations
  - `distanceSquared(Location)` - Optimized distance calculation
  - `clone()` - Create location copy
- **`World`** class - World wrapper
  - `World(native World)` - Wrapper constructor
  - `getName()` - Get world name
  - `getNativeWorld()` - Access native Hytale world object

#### Player Teleportation & Location API
- **`Player.getLocation()`** - Get player's current location
  - Thread-safe access using `world.execute()` with `CountDownLatch`
  - Returns `Location` with position (x, y, z) and rotation (yaw, pitch)
  - Async-safe with 1 second timeout
- **`Player.teleport(Location)`** - Teleport to location
  - Uses native Hytale `Teleport` component via ECS
  - Updates both position and rotation
  - Thread-safe world execution
- **`Player.teleport(double x, double y, double z)`** - Teleport to coordinates
  - Convenience overload maintaining current rotation
- **Player Position Helpers**:
  - `getPositionX()` - Get X coordinate
  - `getPositionY()` - Get Y coordinate
  - `getPositionZ()` - Get Z coordinate
  - `getYaw()` - Get yaw rotation
  - `getPitch()` - Get pitch rotation

#### 📝 Config System
- **`Config`** interface - Main configuration interface
  - `get(String path)` - Get value by path
  - `getString(String path)` - Get string value
  - `getInt(String path)` - Get integer value
  - `getDouble(String path)` - Get double value
  - `getBoolean(String path)` - Get boolean value
  - `getList(String path)` - Get list value
  - `getStringList(String path)` - Get string list
  - `getSection(String path)` - Get config section
  - `set(String path, Object value)` - Set value
  - `save()` / `reload()` - File operations
  - `contains(String path)` - Check if key exists
  - All methods support default values
- **`ConfigSection`** interface - Nested sections support
  - Same read methods as Config for scoped access
  - `getKeys(boolean deep)` - Get all keys in section
  - `getValues()` - Get all values as map
- **`YamlConfig`** class - YAML implementation
  - Custom YAML parser (no external dependencies)
  - Supports nested maps and lists
  - 2-space indentation
  - Auto-quoting for special characters
  - Default values system
- **SimplePlugin integration**:
  - `getConfig()` - Get plugin config (auto-loads from `mods/<PluginName>/config.yml`)
  - `saveConfig()` - Save config to disk
  - `reloadConfig()` - Reload from disk
  - `saveDefaultConfig()` - Create config with defaults
  - `getDataFolder()` - Get plugin data folder (Windows-safe names)

### Changed
- Updated `@version` tags to 1.0.4 in all classes

---

## [1.0.3] - 2026-01-17

### Added

#### 🕐 Scheduler System
- **`Scheduler`** class - Task scheduling and execution
  - `runTask(Runnable)` - Execute task immediately
  - `runTaskLater(Runnable, delayMillis)` - Execute after delay
  - `runTaskTimer(Runnable, initialDelay, periodMillis)` - Repeating tasks
  - `runTaskAsync(Runnable)` - Async execution (non-blocking)
  - `runTaskAsync(Callable<T>)` - Async with return value
- **`ScheduledTask`** class - Task control wrapper
  - `cancel()` - Cancel task
  - `cancelAndInterrupt()` - Force cancel
  - `isCancelled()`, `isDone()`, `isActive()` - Status checks
- `SimplePlugin.getScheduler()` - Access plugin scheduler
- Automatic scheduler lifecycle management (shutdown on plugin disable)

#### 🔐 Permission System
- **`Permission`** class - Permission object wrapper
  - `Permission.of(String)` - Create from node
  - `Permission.fromNative(String)` - Convert from Hytale native
  - `Permission.forCommand(namespace, command)` - Command permissions
  - `Permission.hytaleCommand(command)` - Hytale native command perms
  - `toNative()` - Convert to native string
  - `getParent()` - Get parent permission
  - `isChildOf()`, `isParentOf()` - Hierarchy checks
- **`PermissionManager`** singleton - Centralized permission storage
  - Stores permissions by player UUID
  - Thread-safe with `ConcurrentHashMap`
  - Persists across Player instance creations
  - Supports wildcard permissions (`*`)
  - Supports hierarchical permissions (parent grants children)

#### 👤 Enhanced Player API
- `Player.addPermission(Permission)` - Add permission to player
- `Player.addPermission(String)` - Add permission by node
- `Player.removePermission(Permission)` - Remove permission
- `Player.removePermission(String)` - Remove permission by node
- `Player.hasPermission(Permission)` - Check permission (object)
- `Player.getPermissions()` - Get all player permissions
- `Player.clearPermissions()` - Remove all permissions

#### 📊 Player Stats API
- **Health** - `getHealth()`, `setHealth(float)`
- **Stamina** - `getStamina()`, `setStamina(float)`
- **Oxygen** - `getOxygen()`, `setOxygen(float)`
- **Mana** - `getMana()`, `setMana(float)`
- **Signature Energy** - `getSignatureEnergy()`, `setSignatureEnergy(float)`
- **Ammo** - `getAmmo()`, `setAmmo(float)`
- Thread-safe using `world.execute()` internally
- Async setters, blocking getters (max 5s timeout)

#### 🛠️ Command Utilities
- **`CommandUtils`** class - Command helper methods
  - `isPlayer(CommandContext)` - Check if sender is player
  - `getPlayer(CommandContext)` - Get Player wrapper (nullable)
  - `requirePlayer(CommandContext)` - Get Player or throw exception

### Changed
- Updated `@version` tags to 1.0.3 in affected classes
- `Player.hasPermission()` now delegates to `PermissionManager`
- Permission storage moved from Player instances to centralized manager

### Technical
- Scheduler uses Java `ScheduledExecutorService` with custom thread pools
- Daemon threads for scheduler (auto-cleanup on shutdown)
- Permission system uses Singleton pattern for global state
- All permission operations are thread-safe
- Permissions stored in `ConcurrentHashMap<UUID, Set<Permission>>`

---

## [1.0.2](https://github.com/Program132/HL/compare/V1.0.1...V1.0.2) - 2026-01-16

### Added
- `GameMode` enum with ADVENTURE and CREATIVE modes
- `Player.getGameMode()` - Get current player game mode
- `Player.setGameMode(GameMode)` - Set player game mode
- `Player.sendTitle(String)` - Send a title to the player
- `Player.sendTitleWithSubtitle(String, String)` - Send a title and subtitle to the player
- Complete Javadoc for all `inventory` classes
- Javadoc for `SimpleCommand` class
- Javadoc for `SimpleListener` interface
- **SwitchActiveSlotEvent** from ECS
- Added `PlayerMouseMotionEvent` and `PlayerMouseButtonEvent` (Player Events)

### Changed
- Updated `@version` tags to 1.0.2 in affected classes
- Enhanced `Player` API documentation
- Improved `PlayerQuitEvent` handling to prevent crashes during async disconnects

### Technical
- Uses `com.hypixel.hytale.protocol.GameMode` for native integration
- GameMode conversion methods: `toNative()` and `fromNative()`
- Migrated ECS event handling to dedicated `EntityEventSystem`

---

## [1.0.1](https://github.com/Program132/HL/compare/V1.0.0...V1.0.1) - 2026-01-15

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

## 1.0.0 - 2026-01-14

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
