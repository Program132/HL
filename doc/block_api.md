# Block API Reference

Complete reference for the HytaleLoader Block API (introduced in v1.0.5).

## Overview

The `Block` class (`fr.hytale.loader.api.Block`) is an Object-Oriented wrapper representing a block at specific coordinates in a world. It allows you to inspect and modify blocks easily.

## Getting a Block

You can retrieve a block reference from a `World` object using coordinates or a `Location`.

```java
// From coordinates
Block block = world.getBlockAt(100, 64, 200);

// From a Location
Block block = world.getBlockAt(player.getLocation());
```

> **Note:** `getBlockAt` never returns null (unless the location is in another world). It returns a reference to the block at those coordinates, even if it is "hytale:air".

## Inspecting Blocks

Once you have a `Block` object, you can get its information.

```java
// Get the block type ID (e.g. "hytale:stone", "hytale:grass")
String type = block.getType();

// Check coordinates
int x = block.getX();
int y = block.getY();
int z = block.getZ();

// Get the World
World world = block.getWorld();

// Get as Location
Location loc = block.getLocation();
```

## Modifying Blocks

You can change the type of block directly using the wrapper.

```java
// Change to stone
block.setType("hytale:stone");

// Change to custom block or specific ID
block.setType("Rock_Magma_Cooled");

// Clear block (set to air)
block.setType("hytale:air");
```

## Example: Magic Platform

Here is a simple example that creates a 3x3 glass platform under the player.

```java
@Command(name = "platform")
public void onPlatform(CommandContext ctx) {
    Player p = CommandUtils.requirePlayer(ctx);
    Location loc = p.getLocation();
    
    World world = loc.getWorld();
    int startX = (int) loc.getX();
    int startY = (int) loc.getY() - 1; // Under feet
    int startZ = (int) loc.getZ();
    
    for (int x = -1; x <= 1; x++) {
        for (int z = -1; z <= 1; z++) {
            Block block = world.getBlockAt(startX + x, startY, startZ);
            block.setType("hytale:glass");
        }
    }
    
    p.sendMessage("Platform created!");
}
```

## See Also

- [World API](world_api.md)
- [Location API](location_api.md)
