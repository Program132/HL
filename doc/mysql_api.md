# MySQL API Reference

Complete reference for the HytaleLoader MySQL API.

## Overview

The `MySQLClient` class (`fr.hytale.loader.datastorage.MySQLClient`) provides a simple interface to connect and interact with MySQL databases using connection pooling.

## Getting Started

### Connection

```java
MySQLClient mysql = new MySQLClient(
    "localhost",   // Host
    3306,          // Port
    "minecraft",   // Database name
    "root",        // Username
    "password"     // Password
);

mysql.connect();

if (mysql.isConnected()) {
    plugin.getLogger().info("Connected to MySQL!");
}
```

### Create Database If Not Exists

If the database doesn't exist yet, create it first:

```java
// Create database if it doesn't exist
MySQLClient.createDatabaseIfNotExists(
    "localhost",
    3306,
    "minecraft",   // Database to create
    "root",
    "password"
);

// Then connect normally
MySQLClient mysql = new MySQLClient("localhost", 3306, "minecraft", "root", "password");
mysql.connect();
```

### Disconnect

```java
@Override
public void onDisable() {
    mysql.disconnect();
}
```

## Creating Tables

```java
// Create a table
mysql.execute(
    "CREATE TABLE IF NOT EXISTS players (" +
    "uuid VARCHAR(36) PRIMARY KEY, " +
    "name VARCHAR(16), " +
    "coins INT DEFAULT 0, " +
    "kills INT DEFAULT 0" +
    ")"
);

// Check if table exists
if (mysql.tableExists("players")) {
    // Table exists
}
```

## Insert Operations

### Single Insert

```java
// Insert with parameters
mysql.execute(
    "INSERT INTO players (uuid, name, coins) VALUES (?, ?, ?)",
    playerUUID, playerName, 1000
);

// Insert and get generated ID
long id = mysql.insert(
    "INSERT INTO stats (player_uuid, kills) VALUES (?, ?)",
    playerUUID, 10
);
```

### Batch Insert

```java
List<Object[]> batch = new ArrayList<>();
batch.add(new Object[]{uuid1, "Steve", 1000});
batch.add(new Object[]{uuid2, "Alex", 1500});
batch.add(new Object[]{uuid3, "Herobrine", 2000});

mysql.executeBatch(
    "INSERT INTO players (uuid, name, coins) VALUES (?, ?, ?)",
    batch
);
```

## Query Operations

### Get Multiple Rows

```java
List<Map<String, Object>> players = mysql.query(
    "SELECT * FROM players WHERE coins > ?",
    500
);

for (Map<String, Object> row : players) {
    String name = (String) row.get("name");
    int coins = (int) row.get("coins");
    plugin.getLogger().info(name + " has " + coins + " coins");
}
```

### Get Single Row

```java
Map<String, Object> player = mysql.queryOne(
    "SELECT * FROM players WHERE uuid = ?",
    playerUUID
);

if (player != null) {
    String name = (String) player.get("name");
    int coins = (int) player.get("coins");
}
```

### Get Single Value

```java
Object result = mysql.queryValue(
    "SELECT coins FROM players WHERE uuid = ?",
    playerUUID
);

int coins = result != null ? (int) result : 0;
```

## Update Operations

```java
// Update player coins
int rowsAffected = mysql.execute(
    "UPDATE players SET coins = coins + ? WHERE uuid = ?",
    100, playerUUID
);

// Update multiple fields
mysql.execute(
    "UPDATE players SET coins = ?, kills = ? WHERE uuid = ?",
    1500, 42, playerUUID
);
```

## Delete Operations

```java
// Delete a player
mysql.execute(
    "DELETE FROM players WHERE uuid = ?",
    playerUUID
);

// Delete inactive players
mysql.execute(
    "DELETE FROM players WHERE last_seen < ?",
    System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
);
```

## Transactions

Use transactions when you need multiple operations to succeed or fail together.

```java
boolean success = mysql.transaction(
    new MySQLClient.SQLStatement(
        "UPDATE players SET coins = coins - ? WHERE uuid = ?",
        100, buyerUUID
    ),
    new MySQLClient.SQLStatement(
        "UPDATE players SET coins = coins + ? WHERE uuid = ?",
        100, sellerUUID
    )
);

if (success) {
    plugin.getLogger().info("Transaction completed!");
}
```

## Common Use Cases

### Player Economy

```java
public class EconomyManager {
    private MySQLClient mysql;
    
    public void setCoins(String uuid, int amount) {
        mysql.execute(
            "UPDATE players SET coins = ? WHERE uuid = ?",
            amount, uuid
        );
    }
    
    public int getCoins(String uuid) {
        Object result = mysql.queryValue(
            "SELECT coins FROM players WHERE uuid = ?",
            uuid
        );
        return result != null ? (int) result : 0;
    }
    
    public void addCoins(String uuid, int amount) {
        mysql.execute(
            "UPDATE players SET coins = coins + ? WHERE uuid = ?",
            amount, uuid
        );
    }
}
```

### Player Stats

```java
public void savePlayerStats(String uuid, int kills, int deaths) {
    mysql.execute(
        "INSERT INTO stats (uuid, kills, deaths) VALUES (?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE kills = ?, deaths = ?",
        uuid, kills, deaths, kills, deaths
    );
}

public Map<String, Object> loadPlayerStats(String uuid) {
    return mysql.queryOne(
        "SELECT * FROM stats WHERE uuid = ?",
        uuid
    );
}
```

### Leaderboard

```java
public List<Map<String, Object>> getTopPlayers(int limit) {
    return mysql.query(
        "SELECT name, coins FROM players ORDER BY coins DESC LIMIT ?",
        limit
    );
}
```

## See Also

- [Redis API](redis_api.md)
- [Player API](player_api.md)
- [Config System](config_api.md)
