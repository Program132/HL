# Event System

HytaleLoader provides an annotation-based event system similar to Bukkit/Spigot.

## The `@EventHandler` Annotation

To listen for an event, simply create a method and annotate it with `@EventHandler`. The method must have exactly one parameter: the event you want to listen to.

### Example

```java
import fr.hytale.loader.event.EventHandler;
import com.hypixel.hytale.server.core.event.events.BootEvent;

public class MyListener implements SimpleListener {

    @EventHandler
    public void onServerBoot(BootEvent event) {
        System.out.println("The server has booted!");
    }
}
```

## Registering Listeners

### In Main Class
If your listener methods are in your main class (extending `SimplePlugin`), they are automatically registered when the plugin starts!

### In Separate Classes
If you create separate listener classes, they must implement the marker interface `SimpleListener`. You can then register them in your `onEnable` method:

```java
@Override
public void onEnable() {
    registerListener(new MyListener());
}
```

## Supported Events

You can listen to any event that extends `IBaseEvent` (synchronous) or `IAsyncEvent` (asynchronous). The library automatically detects the type and registers it correctly.
