# GameMode API Reference

**Version:** 1.0.2 (In Development)  
**Status:** 🚧 Work in Progress

Complete reference for the HytaleLoader GameMode API.

## Overview

The `GameMode` enum (`fr.hytale.loader.api.GameMode`) provides a simplified wrapper around Hytale's native game mode system.

## Available Game Modes

```java
GameMode.SURVIVAL   // Survival mode
GameMode.CREATIVE   // Creative mode
```

**Note:** Hytale currently supports only 2 game modes. Adventure and Spectator modes are defined for future compatibility but may not be functional.

## Basic Usage

### Getting Player's Game Mode

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    GameMode mode = player.getGameMode();
    
    if (mode == GameMode.CREATIVE) {
        player.sendMessage("You are in creative mode!");
    } else {
        player.sendMessage("You are in survival mode!");
    }
}
```

### Setting Player's Game Mode

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    // Set to creative mode
    player.setGameMode(GameMode.CREATIVE);
    player.sendMessage("Game mode changed to CREATIVE");
}
```

## Enum Methods

### getId()
```java
int id = GameMode.CREATIVE.getId();
// Returns: 1
```
Returns the numeric ID of the game mode.

**IDs:**
- `SURVIVAL` = 0
- `CREATIVE` = 1

### toNative()
```java
com.hypixel.hytale.protocol.GameMode nativeMode = gameMode.toNative();
```
Converts the HytaleLoader GameMode to a native Hytale GameMode.

### fromNative(GameMode)
```java
com.hypixel.hytale.protocol.GameMode nativeMode = ...;
GameMode mode = GameMode.fromNative(nativeMode);
```
Converts a native Hytale GameMode to a HytaleLoader GameMode.

Returns `GameMode.SURVIVAL` if the native mode is null or unknown.

### fromId(int)
```java
GameMode mode = GameMode.fromId(1);
// Returns: GameMode.CREATIVE
```
Gets a GameMode by its numeric ID.

Returns `GameMode.SURVIVAL` if the ID is invalid.

## Complete Examples

### Game Mode Toggle Command

```java
@Command(name = "togglegm", description = "Toggle between survival and creative")
public void toggleGameMode(CommandContext context) {
    // Get player from context
    PlayerRef playerRef = context.getPlayerRef();
    Player player = new Player(
        playerRef.getComponent(com.hypixel.hytale.server.core.entity.entities.Player.getComponentType()),
        playerRef
    );
    
    // Toggle mode
    GameMode current = player.getGameMode();
    GameMode newMode = (current == GameMode.SURVIVAL) ? GameMode.CREATIVE : GameMode.SURVIVAL;
    
    player.setGameMode(newMode);
    player.sendMessage("Game mode changed to " + newMode);
}
```

### Auto-Creative on Join

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    // Set all players to creative on join
    player.setGameMode(GameMode.CREATIVE);
    player.sendMessage("Welcome! You are now in creative mode.");
}
```

### Game Mode Checker

```java
@EventHandler
public void onBlockBreak(BreakBlockEvent event) {
    Player player = event.getPlayer();
    
    // Only allow breaking blocks in creative
    if (player.getGameMode() != GameMode.CREATIVE) {
        event.setCancelled(true);
        player.sendMessage("You can only break blocks in creative mode!");
    }
}
```

## Technical Details

### Native Integration

The GameMode API wraps Hytale's native `com.hypixel.hytale.protocol.GameMode` class.

Internally, `Player.setGameMode()` uses:
```java
com.hypixel.hytale.server.core.entity.entities.Player.setGameMode(
    ref,
    gameMode.toNative(),
    componentAccessor
);
```

### Error Handling

The `setGameMode()` method fails silently if:
- The player is not in a valid world
- The player reference is invalid
- The game mode cannot be applied

Always check if the operation succeeded if critical:
```java
GameMode before = player.getGameMode();
player.setGameMode(GameMode.CREATIVE);
GameMode after = player.getGameMode();

if (after != GameMode.CREATIVE) {
    player.sendMessage("Failed to change game mode!");
}
```

## Limitations

1. **Limited Modes**: Only SURVIVAL and CREATIVE are fully functional in current Hytale
2. **World Requirement**: Player must be in a valid world to change game mode
3. **No Callbacks**: No event is fired when game mode changes

## Version History

- **v1.0.2** (In Development): Initial GameMode API release

## See Also

- [Player API](player_api.md)
- [Events Documentation](events.md)
- [Getting Started](getting_started.md)
