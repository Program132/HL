# Player Stats API Documentation

**Version:** 1.0.3  
**Package:** `fr.hytale.loader.api`

The Player Stats API provides methods to read and modify player statistics such as health, stamina, oxygen, mana, signature energy, and ammo.

---

## Overview

All stat methods are **thread-safe** and use `world.execute()` internally to ensure proper access to the Hytale ECS components from any thread (including command threads).

**Important Notes:**
- All stat values are `float` type
- Methods return `0.0f` if the stat is unavailable
- Setters execute asynchronously in the world thread
- Getters wait up to 5 seconds for the result

---

## Available Stats

### Health ❤️

```java
// Get player's current health
float health = player.getHealth();

// Set player's health
player.setHealth(20.0f);
```

**Use Cases:**
- Damage/healing systems
- Health-based game mechanics
- Custom death/revival systems

---

### Stamina ⚡

```java
// Get player's current stamina
float stamina = player.getStamina();

// Set player's stamina
player.setStamina(100.0f);
```

**Use Cases:**
- Sprint/dodge mechanics
- Action costs (e.g., special abilities)
- Fatigue systems

---

### Oxygen 💨

```java
// Get player's current oxygen
float oxygen = player.getOxygen();

// Set player's oxygen
player.setOxygen(300.0f);
```

**Use Cases:**
- Underwater breathing mechanics
- Suffocation effects
- Air quality systems

---

### Mana 🔮

```java
// Get player's current mana
float mana = player.getMana();

// Set player's mana
player.setMana(50.0f);
```

**Use Cases:**
- Magic systems
- Spell costs
- Mana regeneration mechanics

---

### Signature Energy ⭐

```java
// Get player's signature energy
float energy = player.getSignatureEnergy();

// Set player's signature energy
player.setSignatureEnergy(75.0f);
```

**Use Cases:**
- Ultimate abilities
- Special attack meters
- Signature move systems

---

### Ammo 🔫

```java
// Get player's current ammo
float ammo = player.getAmmo();

// Set player's ammo
player.setAmmo(30.0f);
```

**Use Cases:**
- Weapon ammunition
- Consumable projectiles
- Arrow/bullet management

---

## Complete Examples

### Example 1: Healing System

```java
@EventHandler
public void onPlayerDamage(PlayerDamageEvent event) {
    Player player = event.getPlayer();
    
    // Check if player has healing potion effect
    if (player.hasPermission("potions.healing")) {
        float currentHealth = player.getHealth();
        player.setHealth(currentHealth + 5.0f);
        player.sendMessage("+5 HP (Healing Potion)");
    }
}
```

---

### Example 2: Stamina-Based Sprint

```java
public void onPlayerSprint(Player player) {
    float stamina = player.getStamina();
    
    if (stamina >= 10.0f) {
        // Allow sprint
        player.setStamina(stamina - 10.0f);
        player.sendMessage("Sprinting! Stamina: " + (stamina - 10.0f));
    } else {
        // Prevent sprint
        player.sendMessage("Not enough stamina!");
    }
}
```

---

### Example 3: Mana Cost System

```java
public boolean castSpell(Player player, float manaCost) {
    float currentMana = player.getMana();
    
    if (currentMana >= manaCost) {
        player.setMana(currentMana - manaCost);
        player.sendMessage("Spell cast! Mana: " + (currentMana - manaCost));
        return true;
    } else {
        player.sendMessage("Not enough mana! Need " + manaCost);
        return false;
    }
}
```

---

### Example 4: Custom Death System

```java
@EventHandler
public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getPlayer();
    
    // Save current stats before death
    float health = player.getHealth();
    float stamina = player.getStamina();
    
    getLogger().info(player.getName() + " died with " + health + " HP");
    
    // Respawn with half stats
    getScheduler().runTaskLater(() -> {
        player.setHealth(10.0f);  // Half health
        player.setStamina(50.0f); // Half stamina
        player.sendMessage("You respawned with reduced stats!");
    }, 100); // 5 seconds delay (100 ticks)
}
```

---

### Example 5: Multi-Stat Display Command

```java
@Command(name = "stats", description = "Show your stats")
public void onStats(CommandContext ctx) {
    if (!CommandUtils.isPlayer(ctx)) {
        ctx.sendMessage(Message.raw("Players only!"));
        return;
    }
    
    Player player = CommandUtils.getPlayer(ctx);
    
    player.sendMessage("=== Your Stats ===");
    player.sendMessage("Health: " + player.getHealth());
    player.sendMessage("Stamina: " + player.getStamina());
    player.sendMessage("Oxygen: " + player.getOxygen());
    player.sendMessage("Mana: " + player.getMana());
    player.sendMessage("Energy: " + player.getSignatureEnergy());
    player.sendMessage("Ammo: " + player.getAmmo());
}
```

---

### Example 6: Oxygen Depletion in Custom Zone

```java
public void tickCustomZone(Player player) {
    // Deplete oxygen in toxic zone
    float oxygen = player.getOxygen();
    
    if (oxygen > 0) {
        player.setOxygen(oxygen - 1.0f);
        
        if (oxygen <= 10.0f) {
            player.sendMessage("WARNING: Low oxygen!");
        }
    } else {
        // Deal damage when out of oxygen
        float health = player.getHealth();
        player.setHealth(health - 2.0f);
        player.sendMessage("Suffocating!");
    }
}
```

---

## Technical Details

### Thread Safety

All stat methods use Hytale's `world.execute()` to ensure thread-safe access:

```java
// Internal implementation (simplified)
private float getStat(int statIndex) {
    CompletableFuture<Float> future = new CompletableFuture<>();
    
    world.execute(() -> {
        // Access ECS components in world thread
        EntityStatMap statMap = store.getComponent(ref, ...);
        EntityStatValue value = statMap.get(statIndex);
        future.complete(value.get());
    });
    
    return future.get(5, TimeUnit.SECONDS);
}
```

### Performance Considerations

- **Getters** are blocking (wait for world thread) - use sparingly in tight loops
- **Setters** are non-blocking (fire and forget) - more efficient
- Cache stat values if you need to read them frequently
- Batch multiple stat reads/writes when possible

---

## Best Practices

### ✅ Do's
- Cache stat values if reading multiple times
- Use events (world thread) when possible instead of commands
- Check for valid players before accessing stats
- Handle 0.0f returns gracefully

### ❌ Don'ts
- Don't call getters in tight loops (performance)
- Don't assume stats are always available
- Don't set stats to negative values without validation
- Don't forget that setters are asynchronous

---

## See Also

- [Player API](player_api.md)
- [Event System](events.md)
- [Scheduler API](scheduler_api.md)
- [Command Utils](command_utils.md)
