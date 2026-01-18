# Entity API

The Entity API provides a unified way to interact with all entities in the Hytale world, including players, NPCs, and animals.

## Overview

The base class for all entities is `fr.hytale.loader.api.Entity`. The `Player` class now extends this `Entity` class, inheriting all its capabilities.

### Key Features
- **Unified ID System**: Access entity Network ID and UUID.
- **Location Management**: Get position and teleport entities.
- **World Interaction**: Get the world the entity is in.
- **Lifecycle**: Check validity and remove entities.

## Entity Class

The `Entity` class wraps the native Hytale `Entity` object.

### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getID()` | `int` | Gets the entity's network ID. |
| `getUUID()` | `UUID` | Gets the entity's unique identifier. |
| `getWorld()` | `World` | Gets the world the entity is currently in. |
| `getLocation()` | `Location` | Gets the current location (async safe). |
| `teleport(Location)` | `void` | Teleports the entity to a location. |
| `teleport(x, y, z)` | `void` | Teleports the entity to coordinates. |
| `remove()` | `void` | Removes the entity from the world. |
| `isValid()` | `boolean` | Checks if the entity is valid and not removed. |

### Example Usage

```java
Entity entity = world.getEntity(someId);
if (entity != null) {
    System.out.println("Entity UUID: " + entity.getUUID());
    entity.teleport(100, 70, 100);
}
```

## World Integration

The `World` class has been updated to support entity retrieval.

```java
// Get entity by ID
Entity entity1 = world.getEntity(12345);

// Get entity by UUID
Entity entity2 = world.getEntity(UUID.fromString("..."));

// Check if it's a player
if (entity1 instanceof Player) {
    Player player = (Player) entity1;
    player.sendMessage("Hello!");
}
```

## Player Modifications

The `Player` class now extends `Entity`. This means you can use all `Entity` methods on `Player` objects.

```java
Player player = ...;
player.teleport(10, 20, 30); // Inherited from Entity
int id = player.getID();     // Inherited from Entity
```
