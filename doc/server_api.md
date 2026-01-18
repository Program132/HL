# Server API Documentation

The `Server` class is a utility wrapper providing static methods to interact with the server instance. It allows you to manage players, worlds, and broadcasting messages.

---

## Quick Reference

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getOnlinePlayers()` | `List<Player>` | Returns all currently online players. |
| `getPlayer(String name)` | `Player` | Finds a player by username (case-insensitive). |
| `getPlayer(UUID uuid)` | `Player` | Finds a player by their UUID. |
| `getOnlineCount()` | `int` | Returns the number of connected players. |
| `broadcast(String msg)` | `void` | Sends a message to everyone. |
| `getWorlds()` | `List<World>` | Returns all loaded worlds. |
| `getDefaultWorld()` | `World` | Returns the main/default world. |

---

## Player Management

### Finding Players
You can retrieve players via their username or UUID.

```java
// Get by Name (case-insensitive)
Player player = Server.getPlayer("MyUsername");

// Get by UUID
UUID uuid = UUID.fromString("...");
Player player = Server.getPlayer(uuid);

if (player != null) {
    // Player is online
}
```

### Iterating Online Players
Access the list of all connected players safely.

```java
for (Player p : Server.getOnlinePlayers()) {
    p.sendMessage("Hello everyone!");
}

// Or using streams
Server.getOnlinePlayers().stream()
    .filter(p -> p.getGameMode() == GameMode.SURVIVAL)
    .forEach(p -> p.setHealth(20));
```

### Statistics
Get the current server population.

```java
int count = Server.getOnlineCount();
```

---

## Communication

### Global Broadcast
Send a raw text message to all online players.

```java
Server.broadcast("[Server] Welcome to the server!");
```

### Permission-Based Broadcast
Send a message only to players who hold a specific permission (e.g., admin alerts).

```java
Server.broadcastPermission("[Admin] Server stopping in 5 minutes.", "server.admin.alerts");
```

---

## World Management

### Getting Worlds
Retrieve loaded worlds or specific world instances.

```java
// Get all worlds
List<World> worlds = Server.getWorlds();

// Get specific world by name
World lobby = Server.getWorld("lobby");

// Get the default world (usually "world")
World mainWorld = Server.getDefaultWorld();
```

### Players in World
Find all players currently located in a specific world.

```java
// Get players in a specific World object
List<Player> players = Server.getPlayersInWorld(lobby);

// Or by world name directly
List<Player> netherPlayers = Server.getPlayersInWorld("nether");

Server.broadcast("There are " + players.size() + " players in the lobby.");
```

---

## See Also
- [Player API](player_api.md)
- [World API](world_api.md)
- [Scheduler API](scheduler_api.md)
