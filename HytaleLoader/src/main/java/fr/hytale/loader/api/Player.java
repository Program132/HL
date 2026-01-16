package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import fr.hytale.loader.api.inventory.Inventory;
import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * HytaleLoader wrapper for the native Hytale Player class.
 * <p>
 * This class provides a simplified and extended API for interacting with
 * players,
 * abstracting the native Hytale server implementation. It adds utility methods
 * and provides a cleaner interface for common player operations.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.1
 */
public class Player {

    private final com.hypixel.hytale.server.core.entity.entities.Player nativePlayer;
    private final PlayerRef playerRef;

    /**
     * Constructs a new Player wrapper.
     * 
     * @param nativePlayer the native Hytale player instance
     * @param playerRef    the player reference
     */
    public Player(com.hypixel.hytale.server.core.entity.entities.Player nativePlayer, PlayerRef playerRef) {
        this.nativePlayer = nativePlayer;
        this.playerRef = playerRef;
    }

    /**
     * Gets the native Hytale player instance.
     * <p>
     * Use this if you need direct access to the native API.
     * </p>
     * 
     * @return the wrapped native player
     */
    public com.hypixel.hytale.server.core.entity.entities.Player getNativePlayer() {
        return nativePlayer;
    }

    /**
     * Gets the native Hytale player reference.
     * <p>
     * The PlayerRef is used for operations that don't require the full entity to be
     * loaded,
     * such as sending messages or checking online status.
     * </p>
     * 
     * @return the native player reference
     */
    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    // === Identity & Basic Info ===

    /**
     * Gets the player's username.
     * 
     * @return the username
     */
    public String getName() {
        return playerRef != null ? playerRef.getUsername() : "Unknown";
    }

    /**
     * Gets the player's UUID.
     * 
     * @return the player's unique identifier
     */
    public java.util.UUID getUUID() {
        return playerRef != null ? playerRef.getUuid() : null;
    }

    // === Messaging ===

    /**
     * Sends a message to this player.
     * 
     * @param message the message text to send
     */
    public void sendMessage(String message) {
        if (playerRef != null) {
            playerRef.sendMessage(Message.raw(message));
        }
    }

    /**
     * Sends a formatted message to this player.
     * 
     * @param message the message object to send
     */
    public void sendMessage(Message message) {
        if (playerRef != null) {
            playerRef.sendMessage(message);
        }
    }

    // === Inventory ===

    /**
     * Gets the player's inventory.
     * 
     * @return the player's inventory
     */
    public Inventory getInventory() {
        if (nativePlayer != null) {
            return new Inventory(nativePlayer.getInventory());
        }
        return null;
    }

    // === Game Mode ===

    /**
     * Gets the player's current game mode.
     * 
     * @return the player's game mode, or SURVIVAL if not available
     */
    public GameMode getGameMode() {
        if (nativePlayer != null) {
            com.hypixel.hytale.protocol.GameMode nativeGameMode = nativePlayer.getGameMode();
            return GameMode.fromNative(nativeGameMode);
        }
        return GameMode.ADVENTURE;
    }

    /**
     * Sets the player's game mode.
     * 
     * @param gameMode the new game mode to set
     */
    public void setGameMode(GameMode gameMode) {
        if (nativePlayer != null && gameMode != null && playerRef != null) {
            try {
                // Get the player's reference
                Ref<EntityStore> ref = playerRef
                        .getReference();

                if (ref != null && ref.isValid()) {
                    // Get the world and its store
                    Store<EntityStore> store = ref.getStore();

                    // Use the static setGameMode method
                    com.hypixel.hytale.server.core.entity.entities.Player.setGameMode(
                            ref,
                            gameMode.toNative(),
                            (ComponentAccessor<EntityStore>) store);
                }
            } catch (Exception e) {
                // Silently fail if gamemode cannot be set
            }
        }
    }

    // === Permissions & Operators ===

    /**
     * Checks if the player is an operator.
     * <p>
     * Note: This feature is not yet implemented in Hytale.
     * </p>
     * 
     * @return true if the player has operator permissions
     */
    public boolean isOp() {
        // TODO: Implement when Hytale adds permission system
        return false;
    }

    /**
     * Checks if the player has a specific permission.
     * <p>
     * Note: This feature is not yet implemented in Hytale.
     * </p>
     * 
     * @param permission the permission node to check
     * @return true if the player has the permission
     */
    public boolean hasPermission(String permission) {
        // TODO: Implement when Hytale adds permission system
        return isOp();
    }

    // === Connection ===

    /**
     * Kicks the player from the server.
     * 
     * @param reason the kick reason
     */
    public void kick(String reason) {
        if (playerRef != null) {
            // TODO: Implement when disconnect method is available
            playerRef.sendMessage(Message.raw("§cKicked: " + reason));
        }
    }

    /**
     * Checks if the player is currently online.
     * 
     * @return true if the player is connected
     */
    public boolean isOnline() {
        return playerRef != null && nativePlayer != null;
    }

    /**
     * Show to the player a title at the top of the screen
     *
     * @param title the title you want to show to the player
     */
    public void sendTitle(String title) {
        EventTitleUtil.showEventTitleToPlayer(
                this.playerRef,
                Message.raw(title),
                Message.raw(""),
                true
        );
    }

    /**
     * Show to the player a title at the top of the screen
     *
     *
     * @param title the title you want to show to the player
     * @param subtitle the subtitle you want to show to the player
    **/
    public void sendTitleWithSubtitle(String title, String subtitle) {
        EventTitleUtil.showEventTitleToPlayer(
                this.playerRef,
                Message.raw(title),
                Message.raw(subtitle),
                true
        );
    }

    // === Utility ===

    @Override
    public String toString() {
        return "Player{name=" + getName() + ", uuid=" + getUUID() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Player))
            return false;
        Player other = (Player) obj;
        return getUUID() != null && getUUID().equals(other.getUUID());
    }

    @Override
    public int hashCode() {
        return getUUID() != null ? getUUID().hashCode() : 0;
    }
}
