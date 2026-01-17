package fr.hytale.loader.command;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.hytale.loader.api.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for working with commands in HytaleLoader.
 * <p>
 * Provides helper methods to convert native Hytale command data
 * to HytaleLoader API objects.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.3
 */
public class CommandUtils {

    private CommandUtils() {
        // Utility class
    }

    /**
     * Checks if the command sender is a player.
     * 
     * @param context the command context
     * @return true if the sender is a player
     */
    public static boolean isPlayer(@Nonnull CommandContext context) {
        return context.isPlayer();
    }

    /**
     * Gets the command sender as a HytaleLoader Player.
     * <p>
     * Returns null if the sender is not a player (e.g., console).
     * </p>
     * 
     * @param context the command context
     * @return the Player wrapper, or null if sender is not a player
     */
    @Nullable
    public static Player getPlayer(@Nonnull CommandContext context) {
        if (!context.isPlayer()) {
            return null;
        }

        try {
            com.hypixel.hytale.server.core.entity.entities.Player nativePlayer = context
                    .senderAs(com.hypixel.hytale.server.core.entity.entities.Player.class);

            PlayerRef playerRef = nativePlayer.getPlayerRef();

            return new Player(nativePlayer, playerRef);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the command sender as a Player, or throws an exception.
     * 
     * @param context the command context
     * @return the Player wrapper
     * @throws IllegalStateException if the sender is not a player
     */
    @Nonnull
    public static Player requirePlayer(@Nonnull CommandContext context) {
        Player player = getPlayer(context);
        if (player == null) {
            throw new IllegalStateException("Command sender must be a player");
        }
        return player;
    }
}
