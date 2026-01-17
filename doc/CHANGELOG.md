# HytaleLoader Changelog

All notable changes to this project will be documented in this file.

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
