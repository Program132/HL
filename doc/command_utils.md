# Command Utilities Documentation

**Version:** 1.0.3  
**Package:** `fr.hytale.loader.command`

The CommandUtils class provides helper methods for working with commands in HytaleLoader.

---

## Overview

CommandUtils helps you:
- Check if command sender is a player
- Convert CommandContext to Player wrapper
- Handle player-only commands gracefully

---

## Class: `CommandUtils`

Static utility class for command operations.

### Methods

#### `isPlayer(CommandContext context)`

Checks if the command sender is a player (not console or other sender).

**Parameters:**
- `context` - The command context

**Returns:** `boolean` - true if sender is a player

**Example:**
```java
@Command(name = "fly")
public void onFly(CommandContext ctx) {
    if (!CommandUtils.isPlayer(ctx)) {
        ctx.sendMessage(Message.raw("§cThis command is for players only!"));
        return;
    }
    
    // Command logic...
}
```

---

#### `getPlayer(CommandContext context)`

Gets the command sender as a HytaleLoader Player wrapper.

**Parameters:**
- `context` - The command context

**Returns:** `Player` - The Player wrapper, or `null` if sender is not a player

**Example:**
```java
@Command(name = "info")
public void onInfo(CommandContext ctx) {
    Player player = CommandUtils.getPlayer(ctx);
    
    if (player == null) {
        ctx.sendMessage(Message.raw("§cPlayers only!"));
        return;
    }
    
    player.sendMessage("Your UUID: " + player.getUUID());
    player.sendMessage("Permissions: " + player.getPermissions().size());
}
```

---

#### `requirePlayer(CommandContext context)`

Gets the command sender as a Player, or throws an exception if not a player.

**Parameters:**
- `context` - The command context

**Returns:** `Player` - The Player wrapper (never null)

**Throws:** `IllegalStateException` - if sender is not a player

**Example:**
```java
@Command(name = "fly")
public void onFly(CommandContext ctx) {
    try {
        Player player = CommandUtils.requirePlayer(ctx);
        player.sendMessage("§aFly mode enabled!");
        // Toggle fly logic here
    } catch (IllegalStateException e) {
        ctx.sendMessage(Message.raw("§cThis command requires a player!"));
    }
}
```

---

## Usage Patterns

### Pattern 1: Early Return

Best for commands where player is required.

```java
@Command(name = "creative")
public void onCreative(CommandContext ctx) {
    if (!CommandUtils.isPlayer(ctx)) {
        ctx.sendMessage(Message.raw("§cOnly players can use this!"));
        return;
    }
    
    Player player = CommandUtils.getPlayer(ctx);
    player.setGameMode(GameMode.CREATIVE);
    player.sendMessage("§aGamemode set to Creative!");
}
```

### Pattern 2: Null Check

Good for commands that work differently for console vs players.

```java
@Command(name = "list")
public void onList(CommandContext ctx) {
    Player player = CommandUtils.getPlayer(ctx);
    
    if (player != null) {
        // Player-specific list
        player.sendMessage("§ePlayers near you: " + getNearbyPlayers(player));
    } else {
        // Console list
        ctx.sendMessage(Message.raw("All players: " + getAllPlayers()));
    }
}
```

### Pattern 3: Try-Catch

Useful when you want exception-based flow control.

```java
@Command(name = "tp")
public void onTeleport(CommandContext ctx) {
    try {
        Player player = CommandUtils.requirePlayer(ctx);
        Location target = getTargetLocation(ctx);
        player.teleport(target);
    } catch (IllegalStateException e) {
        ctx.sendMessage(Message.raw("§cTeleport failed: not a player"));
    }
}
```

---

## Complete Examples

### Example 1: Player Info Command

```java
@Command(name = "me", description = "Show your information")
public void onMe(CommandContext ctx) {
    Player player = CommandUtils.getPlayer(ctx);
    
    if (player == null) {
        ctx.sendMessage(Message.raw("§cPlayers only!"));
        return;
    }
    
    player.sendMessage("§6=== Your Info ===");
    player.sendMessage("§eName: §f" + player.getName());
    player.sendMessage("§eUUID: §f" + player.getUUID());
    player.sendMessage("§eGameMode: §f" + player.getGameMode());
    player.sendMessage("§ePermissions: §f" + player.getPermissions().size());
    
    // Show permissions if admin
    if (player.hasPermission("myplugin.admin")) {
        player.sendMessage("§e§lAdmin Permissions:");
        player.getPermissions().forEach(perm -> 
            player.sendMessage("  §7- " + perm.getNode())
        );
    }
}
```

### Example 2: Give Item Command

```java
@Command(name = "give", description = "Give yourself an item")
public void onGive(CommandContext ctx) {
    try {
        Player player = CommandUtils.requirePlayer(ctx);
        
        // Check permission
        if (!player.hasPermission("myplugin.give")) {
            player.sendMessage("§cNo permission!");
            return;
        }
        
        // Get item from args
        String itemId = (String) ctx.get(itemArg);
        int amount = (Integer) ctx.get(amountArg);
        
        // Give item
        ItemStack stack = new ItemStack(itemId, amount);
        player.getInventory().addItem(new Item(stack));
        
        player.sendMessage("§aGave you " + amount + "x " + itemId);
        
    } catch (IllegalStateException e) {
        ctx.sendMessage(Message.raw("§cThis command requires a player!"));
    }
}
```

### Example 3: Gamemodecommand with Target

```java
@Command(name = "gm", description = "Change gamemode")
public void onGamemode(CommandContext ctx) {
    Player executor = CommandUtils.getPlayer(ctx);
    
    if (executor == null) {
        ctx.sendMessage(Message.raw("§cConsole cannot change gamemode!"));
        return;
    }
    
    // Get target (optional)
    Player target = (Player) ctx.get(targetArg); // May be null
    if (target == null) {
        target = executor; // Self
    }
    
    // Permission check
    boolean canOthers = executor.hasPermission("myplugin.gamemode.others");
    if (target != executor && !canOthers) {
        executor.sendMessage("§cYou can't change others' gamemode!");
        return;
    }
    
    // Change gamemode
    GameMode mode = (GameMode) ctx.get(modeArg);
    target.setGameMode(mode);
    
    if (target == executor) {
        executor.sendMessage("§aGamemode changed to " + mode);
    } else {
        executor.sendMessage("§aChanged " + target.getName() + "'s gamemode to " + mode);
        target.sendMessage("§aYour gamemode was changed to " + mode);
    }
}
```

### Example 4: Conditional Player/Console Command

```java
@Command(name = "broadcast", description = "Broadcast a message")
public void onBroadcast(CommandContext ctx) {
    String message = (String) ctx.get(messageArg);
    
    Player sender = CommandUtils.getPlayer(ctx);
    String prefix;
    
    if (sender != null) {
        // Player sender
        if (!sender.hasPermission("myplugin.broadcast")) {
            sender.sendMessage("§cNo permission!");
            return;
        }
        prefix = "§6[§e" + sender.getName() + "§6]";
    } else {
        // Console sender
        prefix = "§4[§cSERVER§4]";
    }
    
    // Broadcast to all
    String broadcast = prefix + " §f" + message;
    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(broadcast));
    
    ctx.sendMessage(Message.raw("§aBroadcast sent!"));
}
```

---

## Integration with Permission System

CommandUtils pairs perfectly with the Permission API:

```java
@Command(name = "admin")
public void onAdmin(CommandContext ctx) {
    Player player = CommandUtils.getPlayer(ctx);
    
    if (player == null) {
        ctx.sendMessage(Message.raw("§cPlayers only!"));
        return;
    }
    
    // Multiple permission checks
    Permission adminPerm = Permission.of("myplugin.admin");
    Permission superPerm = Permission.of("myplugin.admin.super");
    
    if (!player.hasPermission(adminPerm)) {
        player.sendMessage("§cYou need admin permission!");
        return;
    }
    
    if (player.hasPermission(superPerm)) {
        player.sendMessage("§6Welcome, Super Admin!");
        openSuperAdminPanel(player);
    } else {
        player.sendMessage("§eWelcome, Admin!");
        openAdminPanel(player);
    }
}
```

---

## Best Practices

### ✅ Do's
- Use `isPlayer()` for early validation
- Use `getPlayer()` when null is acceptable
- Use `requirePlayer()` for cleaner code when player is required
- Always send feedback to non-players when command is player-only
- Check permissions after getting Player

### ❌ Don'ts
- Don't assume sender is always a player
- Don't forget to handle null from `getPlayer()`
- Don't ignore IllegalStateException from `requirePlayer()`
- Don't cast CommandSender directly (use CommandUtils!)

---

## Error Handling

### Graceful Failure

```java
@Command(name = "fly")
public void onFly(CommandContext ctx) {
    Player player = CommandUtils.getPlayer(ctx);
    
    if (player == null) {
        // Graceful message for console
        ctx.sendMessage(Message.raw("§7[Info] This command requires a player context."));
        return;
    }
    
    // Continue with player logic...
}
```

### Exception-Based

```java
@Command(name = "adventure")
public void onAdventure(CommandContext ctx) {
    try {
        Player player = CommandUtils.requirePlayer(ctx);
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage("§aGamemode set!");
    } catch (IllegalStateException e) {
        // Console tried to use command
        getLogger().warning("Adventure command requires a player sender");
        ctx.sendMessage(Message.raw("§cCannot set gamemode for console!"));
    }
}
```

---

## See Also

- [Command API](command_api.md)
- [Permission API](permission_api.md)
- [Player API](player_api.md)
