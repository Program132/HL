package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;

import com.hypixel.hytale.server.core.universe.PlayerRef;

/**
 * Called when a player takes damage.
 * <p>
 * This event is fired when a player receives damage from any source,
 * including combat, fall damage, environmental hazards, etc.
 * It provides access to the damage information.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.0
 */
public class PlayerDamageEvent implements IEvent<Void> {

    private final Player player;
    private final PlayerRef playerRef;
    private final Damage damage;

    public PlayerDamageEvent(Player player, PlayerRef playerRef, Damage damage) {
        this.player = player;
        this.playerRef = playerRef;
        this.damage = damage;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public String getPlayerName() {
        return playerRef.getUsername();
    }

    public Damage getDamage() {
        return damage;
    }
}


