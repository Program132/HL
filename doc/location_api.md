# Location API Reference

Complete reference for the HytaleLoader Location API.

## Overview

The `Location` class (`fr.hytale.loader.api.Location`) represents a 3D position with rotation in a specific world. It combines coordinates (x, y, z), rotation angles (yaw, pitch), and a world reference into a single object.

## Creating Locations

### Constructor with Position Only
```java
World world = player.getLocation().getWorld();
Location loc = new Location(world, 100.0, 64.0, 200.0);
```
Creates a location with default rotation (0, 0).

### Constructor with Position and Rotation
```java
Location loc = new Location(world, 100.0, 64.0, 200.0, 45.0f, -15.0f);
```
Creates a location with specific yaw and pitch.

**Parameters:**
- `world` - The world this location is in
- `x`, `y`, `z` - Position coordinates (double)
- `yaw` - Horizontal rotation in degrees (float)
- `pitch` - Vertical rotation in degrees (float)

### Using Native World
```java
com.hypixel.hytale.server.core.universe.world.World nativeWorld = /* ... */;
Location loc = new Location(nativeWorld, 100, 64, 200);
```
Can also accept native Hytale world objects directly.

## Position Methods

### Getting Coordinates
```java
double x = location.getX();
double y = location.getY();
double z = location.getZ();
```
Returns the position coordinates.

### Setting Coordinates
```java
location.setX(150.0);
location.setY(80.0);
location.setZ(250.0);
```
Updates individual coordinates.

## Rotation Methods

### Getting Rotation
```java
float yaw = location.getYaw();     // Horizontal rotation (-180 to 180)
float pitch = location.getPitch(); // Vertical rotation (-90 to 90)
```
Returns rotation angles in degrees.

### Setting Rotation
```java
location.setYaw(90.0f);    // Face east
location.setPitch(-45.0f); // Look down 45°
```
Updates rotation angles.

## Version History

- **v1.0.4**: Initial Location API release

## See Also

- [World API](world_api.md)
- [Player API](player_api.md)
- [Config System](config_api.md)
