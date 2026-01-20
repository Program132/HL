# World API Reference

Complete reference for the HytaleLoader World API.

## Overview

The `World` class (`fr.hytale.loader.api.World`) is a lightweight wrapper around Hytale's native world object. It provides simplified access to world information and serves as a reference in Location objects.

## Getting a World Instance

### From Player Location
```java
Player player = event.getPlayer();
Location loc = player.getLocation();
World world = loc.getWorld();
```
Most common way to get a world reference.

### From Native World
```java
com.hypixel.hytale.server.core.universe.world.World nativeWorld = /* ... */;
World world = new World(nativeWorld);
```
Wrap a native Hytale world object.

## Methods

### getName()
```java
String worldName = world.getName();
player.sendMessage("You are in: " + worldName);
```
Returns the name of the world.

**Returns:** `String` - The world name (e.g., "default", "nether", etc.)

### getNativeWorld()
```java
com.hypixel.hytale.server.core.universe.world.World nativeWorld = world.getNativeWorld();

// Use native world for advanced operations
nativeWorld.execute(() -> {
    // Thread-safe world operations
});
```
Returns the underlying native Hytale world object.

**Returns:** `com.hypixel.hytale.server.core.universe.world.World` - Native world instance

**Use Cases:**
- Advanced ECS operations
- Thread-safe world manipulation via `world.execute()`
- Accessing native-only features


## Block Manipulation

The World API allows getting and setting blocks programmatically.

### setBlock()
Change a block at specific coordinates.

```java
// Using coordinates
world.setBlock(100, 64, 200, "Stone_Sandstone_Red_Brick");

// Using Location
world.setBlock(location, "Rock_Magma_Cooled"); // Supports custom IDs
```


### getBlockIdentifier()
Get the identifier of a block as a string.

```java
// Check block type
String blockId = world.getBlockIdentifier(location);
// Returns "hytale:air" if location is empty or invalid

if (blockId.equals("Stone_Sandstone_Red_Brick")) {
    // It's a stone block
}
```

## Entity Management

### spawnEntity()
Spawns an entity of a given type at a specific location.

```java
// Spawn a generic entity (e.g. Antelope)
Entity x = world.spawnEntity(location, "Antelope");

if (x != null) {
    // Entity successfully spawned
    int id = x.getID();
}
```

### getEntity()
Retrieve an entity by its numeric Network ID or UUID.

```java
// Get by Network ID (int)
Entity entity = world.getEntity(12345);

// Get by UUID
Entity entityByUuid = world.getEntity(uuid);
```
These methods are thread-safe and will handle cross-thread synchronization automatically.


## Sound API

### playSound(Location, String, float, float)
```java
// Play a sound in the world (audible to everyone nearby)
world.playSound(location, "SFX_Bow_T1_Block_Impact", 1.0f, 1.0f);
```
Plays a 3D sound at a specific location.

- **Audible to:** All players near the location.
- **Volume**: 1.0 is default.
- **Pitch**: 1.0 is default.

### SoundCategory
You can optionally specify a `SoundCategory` (e.g. `SoundCategory.MUSIC`, `SoundCategory.HOSTILE`) to categorize the sound.

```java
world.playSound(location, "SFX_Bow_T1_Block_Impact", SoundCategory.HOSTILE, 1.0f, 1.0f);
```

```java
world.playSound(location, "SFX_Bow_T1_Block_Impact", SoundCategory.HOSTILE, 1.0f, 1.0f);
```

## Particle API

### playParticle(Location, String)
```java
// Play a particle effect visible to everyone nearby
world.playParticle(location, "Totem_Slow_AttachOnStatue");
```
Plays a particle effect at a specific location using the native `ParticleUtil`.

- **Visible to:** All players within range (~75 blocks).

## Weather API

### setWeather(WeatherType)
```java
// Force sunny weather using Enum
world.setWeather(WeatherType.CLEAR);

// Force rain using Enum
world.setWeather(WeatherType.RAIN);

// Reset to dynamic weather
world.setWeather((WeatherType) null);
```
Sets the forced weather for the world using the `WeatherType` enum.

### setWeather(String)
```java
// Force custom weather by ID
world.setWeather("Zone1_Sunny");

// Reset to dynamic weather
world.setWeather((String) null);
```
Sets the forced weather for the world using a string ID. Useful for custom weathers or ones not in the enum.

### getWeather()
```java
WeatherType weather = world.getWeather();
if (weather == WeatherType.CLEAR) {
    // defaults to clear
}
```
Gets the current forced weather as a `WeatherType`. Returns `null` if dynamic or unknown.

### getWeatherName()
```java
String weatherName = world.getWeatherName();
```
Gets the current forced weather ID as a string, or `null` if using dynamic weather.

---

## Time API

### setTime(Time)
```java
// Set time to NOON (12:00)
world.setTime(Time.NOON);

// Set time to MIDNIGHT (00:00)
world.setTime(Time.MIDNIGHT);
```
Sets the world time using the `Time` enum.

### setTime(float)
```java
// Set time to 6:00 AM (0.25)
world.setTime(0.25f);
```
Sets the world time as a percentage of the day (0.0 to 1.0), where 0.0 is Midnight and 0.5 is Noon.

### setTimePaused(boolean)
```java
// Freeze time
world.setTimePaused(true);

// Resume day/night cycle
world.setTimePaused(false);
```
Pauses or resumes the daylight cycle.

### getTimeHour()
```java
int hour = world.getTimeHour();
player.sendMessage("It is " + hour + "h");
```
Gets the current hour of the day (0-23).

### isDay()
```java
if (world.isDay()) {
    player.sendMessage("It is day time");
}
```
Checks if it is currently day time (between 6:00 and 18:00).

---

## Common Use Cases

### Checking World Name
```java
Location loc = player.getLocation();
if (loc.getWorld().getName().equals("dungeon")) {
    player.sendMessage("You are in a dungeon!");
}
```

## Version History

- **v1.0.4**: Initial World API release

## See Also

- [Location API](location_api.md)
- [Player API](player_api.md)
- [Config System](config_api.md)
