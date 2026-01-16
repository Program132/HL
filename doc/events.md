# Events Reference

HytaleLoader provides a robust event system allowing plugins to react to game events.

## Event Types

### Player Events (`fr.hytale.loader.event.types.player`)

| Event | Description |
|-------|-------------|
| `PlayerJoinEvent` | Fired when a player joins the server. |
| `PlayerQuitEvent` | Fired when a player leaves the server. |
| `PlayerChatEvent` | Fired when a player sends a chat message. |
| `PlayerDamageEvent` | Fired when a player takes damage. |
| `PlayerCraftEvent` | **Deprecated**. Use `CraftRecipeEvent` instead. |

### ECS / World Events (`fr.hytale.loader.event.types.ecs`)

These events are triggered by the Entity Component System (ECS) and handle interactions with the world.

| Event | Description                                          | Cancellable |
|-------|------------------------------------------------------|-------------|
| `BreakBlockEvent` | Fired when a block is broken.                        | Yes |
| `PlaceBlockEvent` | Fired when a block is placed.                        | Yes |
| `UseBlockEvent` | Fired when a block is interacted with (right-click). | Yes |
| `DamageBlockEvent` | Fired when a block is damaged (mining progress).     | Yes |
| `DropItemEvent` | Fired when an item is dropped.                       | Yes |
| `DiscoverZoneEvent` | Fired when a player discovers a new zone.            | Yes |
| `CraftRecipeEvent` | Fired when a recipe is crafted.                      | Yes |
| `SwitchActiveSlotEvent ` | Fired when switching two elements in the inventory.  | Yes |

## Listening to Events

To listen to an event, create a method with the `@EventHandler` annotation in a class that implements `SimpleListener` (like your main plugin class).

```java
public class MyPlugin extends SimplePlugin {
    
    // ... constructor ...

    @EventHandler
    public void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            player.sendMessage("You broke a block: " + event.getBlockType().getId());
        }
        
        // Cancel breaking bedrock
        if (event.getBlockType().getId().equals("bedrock")) {
            event.setCancelled(true);
        }
    }
}
```

## Event Priorities

Events are dispatched to all registered listeners. Currently, there is no priority system; listeners are called in the order they were registered.

## Async Events

Some events, like `PlayerChatEvent`, may be fired asynchronously. Be careful when accessing non-thread-safe APIs within these events.
