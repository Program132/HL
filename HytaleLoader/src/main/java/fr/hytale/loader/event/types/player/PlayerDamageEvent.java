package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import fr.hytale.loader.api.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;

/**
 * Called when a player takes damage.
 * <p>
 * This event is fired when a player receives damage from any source,
 * including combat, fall damage, environmental hazards, etc.
 * It provides access to the damage information.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.0
 */
public class PlayerDamageEvent implements IEvent<Void> {

    private final Player player;
    private final Damage damage;

    /**
     * Constructs a new PlayerDamageEvent.
     * 
     * @param player the HytaleLoader player wrapper
     * @param damage the damage information
     */
    public PlayerDamageEvent(Player player, Damage damage) {
        this.player = player;
        this.damage = damage;
    }

    /**
     * Gets the player who took damage.
     * 
     * @return the HytaleLoader player wrapper
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's username.
     * 
     * @return the player's name
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Gets the damage information.
     * 
     * @return the damage object containing amount, source, etc.
     */
    public Damage getDamage() {
        return damage;
    }
}
