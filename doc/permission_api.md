# Permission API Documentation

**Version:** 1.0.3  
**Package:** `fr.hytale.loader.permission`

The Permission API provides a comprehensive permission management system for HytaleLoader plugins.

---

## Overview

The Permission system features:
- Object-oriented permission handling
- Centralized permission storage (persists across Player instances)
- Hierarchical permission support (parent grants children)
- Wildcard permissions
- Thread-safe operations
- Integration with Hytale's native permission system

---

## Architecture

### Permission Flow

```
Player Object → PermissionManager (Singleton) → Storage (UUID → Set<Permission>)
      ↓                                                    ↑
   UUID Key  ────────────────────────────────────────────┘
```

**Key Point:** Permissions are stored by **UUID**, not by Player object. This ensures permissions persist even when new Player instances are created.

---

## Classes

### `Permission`

Immutable permission object with conversion and hierarchy support.

#### Creating Permissions

##### `Permission.of(String node)`
Creates a permission from a node string.

```java
Permission admin = Permission.of("myplugin.admin");
Permission edit = Permission.of("myplugin.admin.edit");
```

##### `Permission.fromNative(String nativePermission)`
Creates a permission from a Hytale native permission string.

```java
String hytaleP​erm = HytalePermissions.fromCommand("gamemode");
Permission perm = Permission.fromNative(hytaleP​erm);
```

##### `Permission.forCommand(String namespace, String commandName)`
Creates a command permission following the standard naming convention.

```java
Permission cmd = Permission.forCommand("myplugin", "give");
// Creates: "myplugin.command.give"
```

##### `Permission.hytaleCommand(String commandName)`
Creates a Hytale native command permission.

```java
Permission gm = Permission.hytaleCommand("gamemode");
// Creates: "hytale.command.gamemode"
```

#### Permission Methods

##### `toNative()`
Converts the permission to a native Hytale permission string.

```java
Permission perm = Permission.of("myplugin.admin");
String node = perm.toNative(); // "myplugin.admin"
```

##### `getNode()`
Gets the permission node string.

##### `getParent()`
Gets the parent permission (null if none).

```java
Permission child = Permission.of("myplugin.admin.edit");
Permission parent = child.getParent(); // Permission("myplugin.admin")
```

##### `isChildOf(Permission parent)`
Checks if this permission is a child of another.

```java
Permission admin = Permission.of("myplugin.admin");
Permission edit = Permission.of("myplugin.admin.edit");

edit.isChildOf(admin); // true
```

##### `isParentOf(Permission child)`
Checks if this permission is a parent of another.

---

### `PermissionManager`

Singleton class that manages all player permissions.

#### Accessing the Manager

```java
PermissionManager manager = PermissionManager.getInstance();
```

#### Methods

##### `hasPermission(UUID playerUUID, Permission permission)`
Checks if a player has a permission.

```java
UUID uuid = player.getUUID();
Permission perm = Permission.of("myplugin.admin");

if (manager.hasPermission(uuid, perm)) {
    // Player has permission
}
```

**Features:**
- Checks exact permission
- Checks parent permissions (hierarchy)
- Checks wildcard (`*`)

##### `addPermission(UUID playerUUID, Permission permission)`
Adds a permission to a player.

```java
manager.addPermission(player.getUUID(), Permission.of("myplugin.vip"));
```

##### `removePermission(UUID playerUUID, Permission permission)`
Removes a permission from a player. Returns `true` if removed.

##### `getPermissions(UUID playerUUID)`
Gets all permissions for a player as an unmodifiable set.

```java
Set<Permission> perms = manager.getPermissions(player.getUUID());
perms.forEach(p -> System.out.println(p.getNode()));
```

##### `clearPermissions(UUID playerUUID)`
Removes all permissions for a player.

##### `clearAll()`
Removes all permissions for all players. **Use with caution!**

##### `getPlayerCount()`
Gets the number of players with stored permissions.

---

### `Player` Integration

The Player class provides convenient methods that delegate to PermissionManager.

#### Player Permission Methods

##### `hasPermission(String permission)`
```java
if (player.hasPermission("myplugin.admin")) {
    // Player has admin permission
}
```

##### `hasPermission(Permission permission)`
```java
Permission perm = Permission.of("myplugin.vip");
if (player.hasPermission(perm)) {
    // Player has VIP permission
}
```

##### `addPermission(Permission permission)` / `addPermission(String permission)`
```java
player.addPermission(Permission.of("myplugin.fly"));
player.addPermission("myplugin.god"); // String variant
```

##### `removePermission(Permission permission)` / `removePermission(String permission)`
```java
boolean removed = player.removePermission("myplugin.temp");
```

##### `getPermissions()`
```java
Set<Permission> perms = player.getPermissions();
```

##### `clearPermissions()`
```java
player.clearPermissions();
```

---

## Permission Hierarchy

Permissions support parent-child relationships using dot notation.

### Example

```java
// Give parent permission
player.addPermission("myplugin.admin");

// Player automatically has ALL child permissions:
player.hasPermission("myplugin.admin.edit");   // ✅ true
player.hasPermission("myplugin.admin.delete"); // ✅ true
player.hasPermission("myplugin.admin.view");   // ✅ true

// But NOT unrelated permissions:
player.hasPermission("myplugin.user"); // ❌ false
```

### Wildcard Permission

The `*` permission grants ALL permissions:

```java
player.addPermission("*");
player.hasPermission("anything.you.want"); // ✅ Always true
```

---

## Complete Examples

### Example 1: Admin Command with Permission Check

```java
@Command(name = "kick", description = "Kick a player")
public void onKick(CommandContext ctx) {
    Player admin = CommandUtils.requirePlayer(ctx);
    
    // Check permission
    if (!admin.hasPermission("myplugin.kick")) {
        admin.sendMessage("You don't have permission!");
        return;
    }
    
    // Command logic...
   Player target = getTargetPlayer(ctx);
    target.kick("Kicked by admin");
}
```

### Example 2: VIP System

```java
public class VIPSystem {
    
    private final Permission VIP_PERM = Permission.of("server.vip");
    private final Permission VIP_PLUS = Permission.of("server.vip.plus");
    
    public void giveVIP(Player player, boolean isPlus) {
        if (isPlus) {
            player.addPermission(VIP_PLUS);
            player.sendMessage("You are now VIP+!");
        } else {
            player.addPermission(VIP_PERM);
            player.sendMessage("You are now VIP!");
        }
    }
    
    public boolean isVIP(Player player) {
        return player.hasPermission(VIP_PERM);
    }
    
    public boolean isVIPPlus(Player player) {
        return player.hasPermission(VIP_PLUS);
    }
}
```

### Example 3: Permission Groups

```java
public class PermissionGroups {
    
    public void setGroup(Player player, String group) {
        // Clear existing groups
        player.getPermissions().stream()
            .filter(p -> p.getNode().startsWith("group."))
            .forEach(player::removePermission);
        
        // Add new group
        Permission groupPerm = Permission.of("group." + group.toLowerCase());
        player.addPermission(groupPerm);
        
        // Add group default permissions
        switch (group.toLowerCase()) {
            case "admin":
                player.addPermission("myplugin.admin");
                player.sendMessage("[ADMIN] rank granted!");
                break;
            case "mod":
                player.addPermission("myplugin.moderator");
                player.sendMessage("[MOD] rank granted!");
                break;
            case "vip":
                player.addPermission("myplugin.vip");
                player.sendMessage("[VIP] rank granted!");
                break;
        }
    }
    
    public String getGroup(Player player) {
        return player.getPermissions().stream()
            .map(Permission::getNode)
            .filter(node -> node.startsWith("group."))
            .map(node -> node.substring(6)) // Remove "group." prefix
            .findFirst()
            .orElse("default");
    }
}
```

### Example 4: Database Integration

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUUID();
    
    // Load permissions from database (async)
    getScheduler().runTaskAsync(() -> {
        List<String> perms = database.loadPermissions(uuid);
        
        // Apply permissions (sync)
        getScheduler().runTask(() -> {
            perms.forEach(player::addPermission);
            player.sendMessage("Permissions loaded!");
        });
    });
}

@EventHandler
public void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUUID();
    
    // Save permissions to database
    Set<Permission> perms = player.getPermissions();
    List<String> nodes = perms.stream()
        .map(Permission::getNode)
        .collect(Collectors.toList());
    
    getScheduler().runTaskAsync(() -> {
        database.savePermissions(uuid, nodes);
    });
}
```

---

## Best Practices

### ✅ Do's
- Use Permission objects for better type safety
- Use hierarchy (e.g., `myplugin.admin.*` grants all admin perms)
- Store permissions in PermissionManager (automatic)
- Check permissions before executing sensitive commands
- Use meaningful permission names (`plugin.feature.action`)

### ❌ Don'ts
- Don't store permissions in Player objects directly
- Don't forget that permissions persist across Player instances
- Don't hardcode permission strings everywhere (use constants)
- Don't give wildcard `*` permission lightly

---

## Naming Conventions

### Recommended Format
```
<plugin>.<category>.<permission>
```

### Examples
```java
// Commands
Permission.forCommand("myplugin", "give")    // myplugin.command.give
Permission.forCommand("myplugin", "tp")      // myplugin.command.tp

// Features
Permission.of("myplugin.fly")                // Flight ability
Permission.of("myplugin.god")                // God mode
Permission.of("myplugin.admin.edit")         // Admin edit tools

// Groups
Permission.of("group.admin")                 // Admin group
Permission.of("group.moderator")             // Moderator group
```

---

##Thread Safety

- All PermissionManager operations are thread-safe
- Uses `ConcurrentHashMap` for storage
- Safe to call from async tasks
- No locking required

---

## Performance Notes

- O(1) permission lookup by UUID
- Wildcard and hierarchy checks are O(d) where d = depth
- Thread-safe without blocking
- Minimal memory overhead per player

---

## See Also

- [Scheduler API](scheduler_api.md)
- [Player API](player_api.md)
- [Command API](command_api.md)
