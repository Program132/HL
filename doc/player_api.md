# Player API Reference

Complete reference for the HytaleLoader Player API.

## Overview

The `Player` class (`fr.hytale.loader.api.Player`) is a wrapper around Hytale's native player entity, providing a simplified and enhanced API for plugin developers.

## Getting a Player Instance

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
}
```

## Identity & Basic Info

### getName()
```java
String name = player.getName();
```
Returns the player's username.

### getUUID()
```java
UUID uuid = player.getUUID();
```
Returns the player's unique identifier.

## Messaging

### sendMessage(String)
```java
player.sendMessage("Hello, player!");
```
Sends a plain text message to the player.

### sendMessage(Message)
```java
Message msg = Message.translation("server.welcome");
player.sendMessage(msg);
```
Sends a formatted message object to the player.

### sendTitle(String)
```java
player.sendTitle("Welcome!");
```
Shows a title at the top of the player's screen.

### sendTitleWithSubtitle(String, String)
```java
player.sendTitleWithSubtitle("Welcome!", "Enjoy your stay");
```
Shows a title and a subtitle at the top of the player's screen.

## Inventory

### getInventory()
```java
Inventory inv = player.getInventory();
if (inv != null) {
    // Access inventory
}
```
Returns the player's inventory wrapper.

See [Inventory API](inventory_api.md) for more details.

## Permissions

### isOp()
```java
if (player.isOp()) {
    // Player is an operator
}
```
Checks if the player has operator permissions.

**Note:** Not yet fully implemented in Hytale.

### hasPermission(String)
```java
if (player.hasPermission("myplugin.admin")) {
    // Player has the permission
}
```
Checks if the player has a specific permission.

**Note:** Not yet fully implemented in Hytale. Currently returns `isOp()`.

## Connection

### kick(String)
```java
player.kick("You have been kicked!");
```
Kicks the player from the server with a reason.

**Note:** Currently sends a message instead of disconnecting.

### isOnline()
```java
if (player.isOnline()) {
    player.sendMessage("You are online!");
}
```
Checks if the player is currently connected to the server.

## Native Access

### getNativePlayer()
```java
com.hypixel.hytale.server.core.entity.entities.Player nativePlayer = player.getNativePlayer();
```
Returns the wrapped native Hytale player instance.

**Note:** May return `null` if the player is offline or during certain events (like `PlayerQuitEvent`).

### getPlayerRef()
```java
PlayerRef ref = player.getPlayerRef();
```
Returns the native player reference. This is generally safe to use even if `getNativePlayer()` returns null.

## Utility Methods

### equals(Object)
```java
if (player1.equals(player2)) {
    // Same player (based on UUID)
}
```
Compares two players based on their UUID.

### hashCode()
```java
int hash = player.hashCode();
```
Returns the hash code based on the player's UUID.

### toString()
```java
String info = player.toString();
// Returns: "Player{name=Username, uuid=...}"
```
Returns a string representation of the player.

## Complete Example

```java
@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    // Welcome message
    player.sendMessage("§aWelcome " + player.getName() + "!");
    player.sendTitleWithSubtitle("Welcome!", "Enjoy your stay");
    
    // Check inventory
    Inventory inv = player.getInventory();
    if (inv != null) {
        player.sendMessage("You have " + inv.getItems().size() + " items");
    }
    
    // Permission check
    if (player.hasPermission("myplugin.vip")) {
        player.sendMessage("§6Thanks for being a VIP!");
    }
}
```

## Version History

- **v1.0.2**: Added `sendTitle` and `sendTitleWithSubtitle`
- **v1.0.1**: Added `getInventory()`
- **v1.0.0**: Initial Player API

## See Also

- [Inventory API](inventory_api.md)
- [Events Documentation](events.md)
