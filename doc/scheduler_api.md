# Scheduler API Documentation

**Version:** 1.0.3  
**Package:** `fr.hytale.loader.scheduler`

The Scheduler API provides task scheduling and execution capabilities for HytaleLoader plugins.

---

## Overview

The Scheduler allows you to:
- Execute tasks immediately or after a delay
- Run repeating tasks at fixed intervals
- Execute tasks asynchronously (non-blocking)
- Cancel scheduled tasks
- Monitor task status

---

## Classes

### `Scheduler`

Main scheduler class for executing tasks.

#### Getting the Scheduler

```java
public class MyPlugin extends SimplePlugin {
    @Override
    public void onEnable() {
        Scheduler scheduler = getScheduler();
    }
}
```

#### Methods

##### `runTask(Runnable task)`
Executes a task immediately on the next available thread.

```java
getScheduler().runTask(() -> {
    // Task code here
    player.sendMessage("Hello!");
});
```

**Returns:** `ScheduledTask`

---

##### `runTaskLater(Runnable task, long delayMillis)`
Executes a task after a specified delay.

```java
getScheduler().runTaskLater(() -> {
    player.sendMessage("5 seconds have passed!");
}, 5000); // 5 seconds
```

**Parameters:**
- `task` - The task to execute
- `delayMillis` - Delay in milliseconds

**Returns:** `ScheduledTask`

---

##### `runTaskTimer(Runnable task, long initialDelayMillis, long periodMillis)`
Executes a task repeatedly with a fixed delay between executions.

```java
ScheduledTask task = getScheduler().runTaskTimer(() -> {
    player.sendMessage("Tick!");
}, 0, 1000); // Every second, starting immediately
```

**Parameters:**
- `task` - The task to execute
- `initialDelayMillis` - Delay before first execution
- `periodMillis` - Period between successive executions

**Returns:** `ScheduledTask`

---

##### `runTaskAsync(Runnable task)`
Executes a task asynchronously on a separate thread pool.

**⚠️ Warning:** Async tasks should not directly modify game state. Use for I/O, database queries, or heavy computations.

```java
getScheduler().runTaskAsync(() -> {
    // Long-running task
    String data = fetchFromDatabase();
    
    // Switch back to sync for game modifications
    getScheduler().runTask(() -> {
        player.sendMessage("Data: " + data);
    });
});
```

**Returns:** `CompletableFuture<Void>`

---

##### `runTaskAsync(Callable<T> task)`
Executes a task asynchronously and returns a result.

```java
CompletableFuture<Integer> future = getScheduler().runTaskAsync(() -> {
    // Compute something
    return calculateScore(player);
});

future.thenAccept(score -> {
    getScheduler().runTask(() -> {
        player.sendMessage("Your score: " + score);
    });
});
```

**Returns:** `CompletableFuture<T>`

---

### `ScheduledTask`

Wrapper for scheduled tasks with control methods.

#### Methods

##### `cancel()`
Cancels the task. Returns `true` if successfully cancelled.

```java
ScheduledTask task = getScheduler().runTaskTimer(() -> {
    // Repeating task
}, 0, 1000);

// Cancel after 10 seconds
getScheduler().runTaskLater(task::cancel, 10000);
```

##### `cancelAndInterrupt()`
Cancels the task and attempts to interrupt if running.

##### `isCancelled()`
Checks if the task was cancelled.

##### `isDone()`
Checks if the task has completed.

##### `isActive()`
Checks if the task is still running or scheduled.

---

## Complete Examples

### Example 1: Countdown Timer

```java
public class CountdownCommand extends SimpleCommand {
    
    @Command(name = "countdown")
    public void onCountdown(CommandContext ctx) {
        Player player = CommandUtils.requirePlayer(ctx);
        
        AtomicInteger counter = new AtomicInteger(5);
        
        ScheduledTask task = getScheduler().runTaskTimer(() -> {
            int count = counter.getAndDecrement();
            
            if (count <= 0) {
                player.sendMessage("Go!");
                task.cancel();
            } else {
                player.sendMessage(count + "...");
            }
        }, 0, 1000); // Every second
    }
}
```

### Example 2: Async Database Query

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    
    // Async database query (non-blocking)
    getScheduler().runTaskAsync(() -> {
        PlayerData data = database.loadPlayer(player.getUUID());
        
        // Switch back to sync thread for game modifications
        getScheduler().runTask(() -> {
            player.sendMessage("Welcome back! Level: " + data.getLevel());
            
            // Apply permissions from database
            data.getPermissions().forEach(player::addPermission);
        });
    });
}
```

### Example 3: Delayed Teleport

```java
public void teleportWithDelay(Player player, Location target) {
    player.sendMessage("Teleporting in 3 seconds...");
    player.sendMessage("Don't move!");
    
    Location startLoc = player.getLocation();
    
    getScheduler().runTaskLater(() -> {
        // Check if player moved
        if (player.getLocation().equals(startLoc)) {
            player.teleport(target);
            player.sendMessage("Teleported!");
        } else {
            player.sendMessage("Teleport cancelled - you moved!");
        }
    }, 3000); // 3 seconds
}
```

### Example 4: Repeating Task with Auto-Cancel

```java
public void startParticleEffect(Player player, int durationSeconds) {
    AtomicInteger timeLeft = new AtomicInteger(durationSeconds);
    
    ScheduledTask task = getScheduler().runTaskTimer(() -> {
        if (timeLeft.getAndDecrement() <= 0) {
            task.cancel();
            return;
        }
        
        // Spawn particles around player
        spawnParticles(player.getLocation());
    }, 0, 50); // Every 50ms (20 times per second)
}
```

---

## Best Practices

### ✅ Do's
- Use async for I/O operations (file, network, database)
- Cancel repeating tasks when no longer needed
- Use `runTask()` to switch back from async to sync
- Keep task code short and focused

### ❌ Don'ts
- Don't modify game state directly from async tasks
- Don't create infinite loops in repeating tasks
- Don't forget to cancel tasks on plugin disable (handled automatically)
- Don't use Thread.sleep() - use delay instead

---

## Thread Safety

- Scheduler operations are thread-safe
- Async tasks run on separate thread pool
- Always use `runTask()` to modify game state from async context
- Scheduler automatically shuts down when plugin is disabled

---

## Performance Notes

- Default thread pools: 4 scheduled threads, 8 async threads
- Daemon threads auto-cleanup on JVM shutdown
- `ScheduledExecutorService` for precise timing
- Thread-safe task management

---

## See Also

- [Permission API](permission_api.md)
- [Player API](player_api.md)
- [Command API](command_api.md)
