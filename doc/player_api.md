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

## Location & Position

### getLocation()
```java
Location loc = player.getLocation();
if (loc != null) {
    double x = loc.getX();
    double y = loc.getY();
    double z = loc.getZ();
    float yaw = loc.getYaw();
    float pitch = loc.getPitch();
    World world = loc.getWorld();
}
```
Returns the player's current location (position + rotation + world).

**Thread-safe:** Uses `world.execute()` with `CountDownLatch` (1 second timeout).

### getPositionX(), getPositionY(), getPositionZ()
```java
double x = player.getPositionX();
double y = player.getPositionY();
double z = player.getPositionZ();
```
Convenience methods to get individual position coordinates.

### getYaw(), getPitch()
```java
float yaw = player.getYaw();    // Horizontal rotation
float pitch = player.getPitch(); // Vertical rotation
```
Get player rotation angles.

## Teleportation

### teleport(Location)
```java
Location target = new Location(world, 100, 64, 200, 0, 0);
player.teleport(target);
```
Teleports the player to the specified location.

- Updates position (x, y, z)
- Updates rotation (yaw, pitch)
- Changes world if different
- Thread-safe using `world.execute()`

**Note:** Uses native Hytale `Teleport` ECS component.

### teleport(double x, double y, double z)
```java
player.teleport(100, 64, 200);
```
Teleports the player to coordinates while maintaining current rotation.

## Game Mode

### getGameMode()
```java
GameMode mode = player.getGameMode();
if (mode == GameMode.CREATIVE) {
    // Player is in creative mode
}
```
Returns the player's current game mode.

### setGameMode(GameMode)
```java
player.setGameMode(GameMode.ADVENTURE);
```
Sets the player's game mode.

Available modes:
- `GameMode.ADVENTURE`
- `GameMode.CREATIVE`

## Player Stats

### Health
```java
float health = player.getHealth();
player.setHealth(20.0f);
```
Gets or sets the player's health.

### Stamina
```java
float stamina = player.getStamina();
player.setStamina(100.0f);
```
Gets or sets the player's stamina.

### Oxygen
```java
float oxygen = player.getOxygen();
player.setOxygen(100.0f);
```
Gets or sets the player's oxygen level.

### Mana
```java
float mana = player.getMana();
player.setMana(50.0f);
```
Gets or sets the player's mana.

### Signature Energy
```java
float energy = player.getSignatureEnergy();
player.setSignatureEnergy(75.0f);
```
Gets or sets the player's signature energy.

### Ammo
```java
float ammo = player.getAmmo();
player.setAmmo(30.0f);
```
Gets or sets the player's ammo count.

**Note:** All stat setters are async, getters block with 5s timeout.

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

### hasPermission(Permission)
```java
Permission perm = Permission.of("myplugin.admin");
if (player.hasPermission(perm)) {
    // Player has the permission
}
```
Checks permission using Permission object.

### addPermission(String / Permission)
```java
player.addPermission("myplugin.vip");
// or
player.addPermission(Permission.of("myplugin.vip"));
```
Adds a permission to the player.

### removePermission(String / Permission)
```java
player.removePermission("myplugin.vip");
```
Removes a permission from the player.

### getPermissions()
```java
Set<Permission> perms = player.getPermissions();
```
Gets all permissions granted to the player.

### clearPermissions()
```java
player.clearPermissions();
```
Removes all permissions from the player.

## Sound API

### playSound(String, float, float)
```java
// Play a sound to the player (2D)
player.playSound("SFX_Bow_T1_Block_Impact", 1.0f, 1.0f);
```
Plays a sound effect to the player. The sound is 2D (no location).

### playSound(String, SoundCategory, float, float)
```java
// Play a sound with a specific category
player.playSound("SFX_Bow_T1_Block_Impact", SoundCategory.MUSIC, 1.0f, 1.0f);
```
Plays a sound to the player with a specific `SoundCategory`.

### playSound(Location, String, float, float)
```java
// Play a sound at a localized position (only this player hears it)
player.playSound(player.getLocation(), "SFX_Bow_T1_Block_Impact", 1.0f, 1.0f);
```
Plays a 3D sound at a specific location, audible only to this player.

---

---

## Particle API

### playParticle(Location, String)
```java
// Play a particle effect only visible to this player
player.playParticle(player.getLocation(), "Totem_Slow_AttachOnStatue");
```
Plays a particle effect at a specific location.

- **Visible to:** Only the player.

---

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
    player.sendMessage("Welcome " + player.getName() + "!");
    player.sendTitleWithSubtitle("Welcome!", "Enjoy your stay");
    
    // Show player location
    Location loc = player.getLocation();
    if (loc != null) {
        player.sendMessage(String.format("Position: %.2f, %.2f, %.2f", 
            loc.getX(), loc.getY(), loc.getZ()));
        player.sendMessage("World: " + loc.getWorld().getName());
    }
    
    // Check inventory
    Inventory inv = player.getInventory();
    if (inv != null) {
        player.sendMessage("You have " + inv.getItems().size() + " items");
    }
    
    // Permission check
    if (player.hasPermission("myplugin.vip")) {
        player.sendMessage("Thanks for being a VIP!");
        
        // Teleport VIP players to spawn
        Location spawn = new Location(loc.getWorld(), 0, 100, 0);
        player.teleport(spawn);
    }
    
    // Show player stats
    player.sendMessage(String.format("Health: %.1f", player.getHealth()));
    player.sendMessage(String.format("Mana: %.1f", player.getMana()));
}
```

## Version History

- **v1.0.4**: Added `getLocation()`, `teleport()`, position helpers
- **v1.0.3**: Added permission management and scheduler access, player stats
- **v1. 0.2**: Added `sendTitle` and `sendTitleWithSubtitle`, game mode support
- **v1.0.1**: Added `getInventory()`
- **v1.0.0**: Initial Player API

## See Also

- [Location API](location_api.md)
- [World API](world_api.md)
- [Inventory API](inventory_api.md)
- [Permission API](permission_api.md)
- [Events Documentation](events.md)
