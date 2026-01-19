# Config System API Documentation

The Config System provides an easy-to-use YAML or JSON configuration API for plugins.

---

## Quick Start

```java
public class MyPlugin extends SimplePlugin {
    
    @Override
    public void onEnable() {
        // Get config (auto-loads from mods/MyPlugin/config.yml or config.json)
        Config config = getConfig();
        
        // Set defaults
        config.addDefault("server.name", "My Server");
        config.addDefault("server.max-players", 20);
        config.addDefault("features.pvp", true);
        
        // Read values
        String name = config.getString("server.name");
        int maxPlayers = config.getInt("server.max-players");
        boolean pvp = config.getBoolean("features.pvp");
        
        getLogger().info("Server: " + name + " (max " + maxPlayers + " players)");
    }
}
```

**Creates** `mods/MyPlugin/config.yml`:
```yaml
server:
  name: My Server
  max-players: 20
features:
  pvp: true
```

---

## Choosing Configuration Format

By default, HytaleLoader uses **YAML** (`config.yml`). 
You can switch to **JSON** (`config.json`) by overriding the `getConfigFormat()` method in your plugin class.

```java
import fr.hytale.loader.config.ConfigFormat;

public class MyPlugin extends SimplePlugin {

    @Override
    public ConfigFormat getConfigFormat() {
        // Use JSON instead of YAML
        return ConfigFormat.JSON;
    }

    @Override
    public void onEnable() {
        // Now getConfig() returns a JsonConfig linked to config.json
        // API usage remains exactly the same!
        getConfig().addDefault("server.name", "My JSON Server");
        saveDefaultConfig();
    }
}
```

**Creates** `mods/MyPlugin/config.json`:
```json
{
  "server": {
    "name": "My JSON Server"
  }
}
```

---

## API Reference

### Reading Values

```java
// String
String value = getConfig().getString("path");
String value = getConfig().getString("path", "default");

// Numbers
int number = getConfig().getInt("path");
int number = getConfig().getInt("path", 0);
double decimal = getConfig().getDouble("path", 1.5);

// Boolean
boolean flag = getConfig().getBoolean("path");
boolean flag = getConfig().getBoolean("path", false);

// Lists
List<?> items = getConfig().getList("path");
List<String> strings = getConfig().getStringList("path");

// Sections
ConfigSection section = getConfig().getSection("path");
if (section != null) {
    String value = section.getString("subkey");
}
```

### Writing Values

```java
// Set values
getConfig().set("server.name", "New Name");
getConfig().set("server.port", 25565);
getConfig().set("features.enabled", true);

// Set lists
List<String> admins = Arrays.asList("Alice", "Bob");
getConfig().set("admins", admins);

// Save to file
try {
    saveConfig();
} catch (IOException e) {
    getLogger().severe("Failed to save: " + e.getMessage());
}
```

### Default Values

```java
@Override
public void onEnable() {
    // Add defaults
    getConfig().addDefault("server.name", "Default Server");
    getConfig().addDefault("server.port", 25565);
    
    // Or use a map
    Map<String, Object> defaults = new HashMap<>();
    defaults.put("feature.enabled", true);
    getConfig().setDefaults(defaults);
    
    // Save defaults if file doesn't exist
    saveDefaultConfig();
}
```

### Checking Keys

```java
// Check if exists
if (getConfig().contains("server.name")) {
    String name = getConfig().getString("server.name");
}

// Get all keys
Set<String> keys = getConfig().getKeys(false);  // Top-level only
Set<String> allKeys = getConfig().getKeys(true);  // Including nested
```

### File Operations

```java
// Reload from disk
try {
    reloadConfig();
} catch (IOException e) {
    getLogger().severe("Reload failed");
}

// Get file path
File configFile = getConfig().getFile();
getLogger().info("Config: " + configFile.getAbsolutePath());

// Get data folder
File dataFolder = getDataFolder();  // mods/<PluginName>/
```

---

## Examples

### Example 1: Feature Toggles

```java
public class MyPlugin extends SimplePlugin {
    
    private boolean autoSave;
    private boolean chatFilter;
    
    @Override
    public void onEnable() {
        loadConfig();
        
        if (autoSave) {
            startAutoSaveTask();
        }
    }
    
    private void loadConfig() {
        getConfig().addDefault("features.auto-save", true);
        getConfig().addDefault("features.chat-filter", false);
        saveDefaultConfig();
        
        autoSave = getConfig().getBoolean("features.auto-save");
        chatFilter = getConfig().getBoolean("features.chat-filter");
    }
}
```

**config.yml:**
```yaml
features:
  auto-save: true
  chat-filter: false
```

### Example 2: Database Config

```java
public class DatabasePlugin extends SimplePlugin {
    
    @Override
    public void onEnable() {
        // Set defaults
        getConfig().addDefault("database.host", "localhost");
        getConfig().addDefault("database.port", 3306);
        getConfig().addDefault("database.name", "hytale");
        getConfig().addDefault("database.username", "root");
        saveDefaultConfig();
        
        // Load config section
        ConfigSection db = getConfig().getSection("database");
        if (db != null) {
            String host = db.getString("host");
            int port = db.getInt("port");
            String name = db.getString("name");
            
            getLogger().info("Connecting to " + host + ":" + port + "/" + name);
        }
    }
}
```

**config.yml:**
```yaml
database:
  host: localhost
  port: 3306
  name: hytale_db
  username: admin
  password: secret
```

### Example 3: Reload Command

```java
@Command(name = "reload", permission = "myplugin.reload")
public void onReload(CommandContext ctx) {
    try {
        reloadConfig();
        ctx.sender().sendMessage(Message.raw("Config reloaded!"));
    } catch (IOException e) {
        ctx.sender().sendMessage(Message.raw("Failed to reload config!"));
    }
}
```

---

## Technical Details

### Supported Types

| Java Type | YAML Example |
|-----------|--------------|
| `String` | `name: "Server"` |
| `Integer` | `port: 25565` |
| `Double` | `multiplier: 1.5` |
| `Boolean` | `enabled: true` |
| `List<?>` | `items: [a, b, c]` |
| `Map` | `section: { key: value }` |

### Path Notation

Use dots to access nested values:

```yaml
server:
  database:
    host: localhost
```

Access as: `config.getString("server.database.host")`

### File Location

### File Location
- Default: `mods/<PluginName>/config.yml` (or `config.json` if configured)
- Plugin names with invalid characters (`:`, `/`, `\`) are replaced with `_`
- Example: `fr.testmod:TestMod` → `mods/fr.testmod_TestMod/config.yml` (or `.json`)

---

## See Also

- [Getting Started](getting_started.md)
- [Commands](commands.md)
- [Scheduler API](scheduler_api.md)
- [Player API](player_api.md)
