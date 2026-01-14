# Standard Events

HytaleLoader provides simplified, standard events for common actions. These events act as wrappers around Hytale's internal events or ECS systems, making them easier to use.

## Available Events

### PlayerJoinEvent
Fired when a player successfully joins the world.
- **Method**: `getPlayer()` returns the `Player` object.
- **Underlying Event**: `AddPlayerToWorldEvent`.

### PlayerQuitEvent
Fired when a player disconnects from the server.
- **Method**: `getPlayer()` returns the `Player` object.
- **Underlying Event**: `PlayerDisconnectEvent`.

### PlayerDamageEvent
Fired when a player takes damage.
- **Method**: `getPlayer()` returns the victim `Player`.
- **Method**: `getDamage()` returns the `Damage` object (use `getAmount()`, `getSource()`, etc.).
- **Note**: This is bridged from the ECS `DamageEventSystem`.

## Usage Example

Register them normally in your main class or listener class using `@EventHandler`.

```java
import fr.hytale.loader.event.EventHandler;
import fr.hytale.loader.event.SimpleListener;
import fr.hytale.loader.event.types.PlayerJoinEvent;
import fr.hytale.loader.event.types.PlayerQuitEvent;
import fr.hytale.loader.event.types.PlayerDamageEvent;

public class MyListener implements SimpleListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        System.out.println(event.getPlayer().getName() + " joined the game!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        System.out.println(event.getPlayer().getName() + " left the game.");
    }

    @EventHandler
    public void onDamage(PlayerDamageEvent event) {
        System.out.println(event.getPlayer().getName() + " took " + event.getDamage().getAmount() + " damage!");
    }
}
```
