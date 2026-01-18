# Redis API Reference

Complete reference for the HytaleLoader Redis API.

## Overview

The `RedisClient` class (`fr.hytale.loader.datastorage.RedisClient`) provides a simple interface to connect and interact with remote Redis databases.

## Getting Started

### Basic Connection

```java
RedisClient redis = new RedisClient("127.0.0.1", 6379);
redis.connect();

// Test connection
if (redis.isConnected()) {
    plugin.getLogger().info("Connected to Redis!");
}
```

### With Authentication

```java
RedisClient redis = new RedisClient("redis.server.com", 6379, "password");
redis.connect();
```

### Disconnect

```java
@Override
public void onDisable() {
    redis.disconnect();
}
```

## String Operations

```java
// Set/Get
redis.set("player:steve:coins", "1000");
String coins = redis.get("player:steve:coins");

// With expiration (seconds)
redis.setex("session:abc", "active", 3600);

// Increment/Decrement
redis.incr("player:steve:kills");
redis.incrBy("player:steve:coins", 50);
redis.decr("player:steve:lives");

// Delete
redis.delete("old:key");
redis.delete("key1", "key2", "key3");

// Check existence
if (redis.exists("player:steve")) {
    // Key exists
}
```

## Hash Operations

Perfect for storing objects with multiple fields.

```java
// Set fields
redis.hset("player:steve", "name", "Steve");
redis.hset("player:steve", "level", "42");

// Get field
String name = redis.hget("player:steve", "name");

// Get all fields
Map<String, String> data = redis.hgetAll("player:steve");

// Check field
if (redis.hexists("player:steve", "guild")) {
    // Field exists
}

// Delete fields
redis.hdel("player:steve", "old_field");
```

## List Operations

```java
// Push to end
redis.rpush("chat:global", "Hello!");

// Push to start
redis.lpush("notifications", "New message!");

// Get range
List<String> messages = redis.lrange("chat:global", 0, -1);

// Get length
long count = redis.llen("chat:global");
```

## Set Operations

```java
// Add members
redis.sadd("online:players", "steve", "alex");

// Check member
if (redis.sismember("online:players", "steve")) {
    // Steve is online
}

// Get all members
Set<String> players = redis.smembers("online:players");

// Remove members
redis.srem("online:players", "alex");
```

## Common Use Cases

### Player Economy

```java
public class Economy {
    private RedisClient redis;
    
    public void addCoins(String player, int amount) {
        redis.incrBy("player:" + player + ":coins", amount);
    }
    
    public int getCoins(String player) {
        String value = redis.get("player:" + player + ":coins");
        return value != null ? Integer.parseInt(value) : 0;
    }
}
```

### Cooldown System

```java
public void setCooldown(String player, String action, int seconds) {
    redis.setex("cooldown:" + player + ":" + action, "1", seconds);
}

public boolean hasCooldown(String player, String action) {
    return redis.exists("cooldown:" + player + ":" + action);
}
```

### Player Stats

```java
public void saveStats(String player, int kills, int deaths) {
    String key = "stats:" + player;
    redis.hset(key, "kills", String.valueOf(kills));
    redis.hset(key, "deaths", String.valueOf(deaths));
    redis.hset(key, "lastSeen", String.valueOf(System.currentTimeMillis()));
}

public Map<String, String> loadStats(String player) {
    return redis.hgetAll("stats:" + player);
}
```

## See Also

- [Player API](player_api.md)
- [Config System](config_api.md)
- [Scheduler API](scheduler_api.md)
