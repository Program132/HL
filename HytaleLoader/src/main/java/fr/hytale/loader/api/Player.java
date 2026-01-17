package fr.hytale.loader.api;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;
import fr.hytale.loader.api.inventory.Inventory;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import fr.hytale.loader.permission.Permission;
import fr.hytale.loader.permission.PermissionManager;

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

    // === Health & Stats ===

    /**
     * Gets the player's current health.
     * 
     * @return the player's current health, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getHealth() {
        return getStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getHealth());
    }

    /**
     * Sets the player's health.
     * 
     * @param health the new health value
     * @since 1.0.3
     */
    public void setHealth(float health) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getHealth(), health);
    }

    /**
     * Gets the player's current stamina.
     * 
     * @return the player's current stamina, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getStamina() {
        return getStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getStamina());
    }

    /**
     * Sets the player's stamina.
     * 
     * @param stamina the new stamina value
     * @since 1.0.3
     */
    public void setStamina(float stamina) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getStamina(), stamina);
    }

    /**
     * Gets the player's current oxygen.
     * 
     * @return the player's current oxygen, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getOxygen() {
        return getStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getOxygen());
    }

    /**
     * Sets the player's oxygen.
     * 
     * @param oxygen the new oxygen value
     * @since 1.0.3
     */
    public void setOxygen(float oxygen) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getOxygen(), oxygen);
    }

    /**
     * Gets the player's current mana.
     * 
     * @return the player's current mana, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getMana() {
        return getStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getMana());
    }

    /**
     * Sets the player's mana.
     * 
     * @param mana the new mana value
     * @since 1.0.3
     */
    public void setMana(float mana) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getMana(), mana);
    }

    /**
     * Gets the player's current signature energy.
     * 
     * @return the player's current signature energy, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getSignatureEnergy() {
        return getStat(
                com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getSignatureEnergy());
    }

    /**
     * Sets the player's signature energy.
     * 
     * @param energy the new signature energy value
     * @since 1.0.3
     */
    public void setSignatureEnergy(float energy) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getSignatureEnergy(),
                energy);
    }

    /**
     * Gets the player's current ammo.
     * 
     * @return the player's current ammo, or 0.0 if unavailable
     * @since 1.0.3
     */
    public float getAmmo() {
        return getStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getAmmo());
    }

    /**
     * Sets the player's ammo.
     * 
     * @param ammo the new ammo value
     * @since 1.0.3
     */
    public void setAmmo(float ammo) {
        setStat(com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes.getAmmo(), ammo);
    }

    /**
     * Gets an entity stat value by its index.
     * 
     * @param statIndex the stat index from DefaultEntityStatTypes
     * @return the stat value, or 0.0 if unavailable
     * @since 1.0.3
     */
    private float getStat(int statIndex) {
        if (nativePlayer == null || playerRef == null) {
            return 0.0f;
        }

        try {
            com.hypixel.hytale.server.core.universe.world.World world = nativePlayer.getWorld();
            if (world == null) {
                return 0.0f;
            }

            // Use CompletableFuture to get result from world thread
            java.util.concurrent.CompletableFuture<Float> future = new java.util.concurrent.CompletableFuture<>();

            world.execute(() -> {
                try {
                    com.hypixel.hytale.component.Store<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> store = world
                            .getEntityStore().getStore();
                    com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> ref = nativePlayer
                            .getReference();

                    if (ref == null || !ref.isValid()) {
                        future.complete(0.0f);
                        return;
                    }

                    com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap entityStatMap = (com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap) store
                            .getComponent(
                                    ref,
                                    com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule.get()
                                            .getEntityStatMapComponentType());

                    if (entityStatMap == null) {
                        future.complete(0.0f);
                        return;
                    }

                    com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue value = entityStatMap
                            .get(statIndex);
                    future.complete((value != null) ? value.get() : 0.0f);
                } catch (Exception e) {
                    future.complete(0.0f);
                }
            });

            // Wait for result (max 5 seconds)
            return future.get(5, java.util.concurrent.TimeUnit.SECONDS);

        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * Sets an entity stat value by its index.
     * 
     * @param statIndex the stat index from DefaultEntityStatTypes
     * @param value     the new value
     * @since 1.0.3
     */
    private void setStat(int statIndex, float value) {
        if (nativePlayer == null || playerRef == null) {
            return;
        }

        try {
            com.hypixel.hytale.server.core.universe.world.World world = nativePlayer.getWorld();
            if (world == null) {
                return;
            }

            // Execute in world thread like ReviveMe does
            world.execute(() -> {
                try {
                    com.hypixel.hytale.component.Store<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> store = world
                            .getEntityStore().getStore();
                    com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> ref = nativePlayer
                            .getReference();

                    if (ref == null || !ref.isValid()) {
                        return;
                    }

                    com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap entityStatMap = (com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap) store
                            .getComponent(
                                    ref,
                                    com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule.get()
                                            .getEntityStatMapComponentType());

                    if (entityStatMap == null) {
                        return;
                    }

                    entityStatMap.setStatValue(statIndex, value);
                } catch (Exception e) {
                    // Silently fail
                }
            });

        } catch (Exception e) {
            // Silently fail
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
        return false;
    }

    /**
     * Checks if the player has a specific permission.
     * 
     * @param permission the permission node to check
     * @return true if the player has the permission
     */
    public boolean hasPermission(String permission) {
        return hasPermission(Permission.of(permission));
    }

    /**
     * Checks if the player has a specific permission.
     * 
     * @param permission the permission to check
     * @return true if the player has the permission
     * @since 1.0.3
     */
    public boolean hasPermission(Permission permission) {
        UUID uuid = getUUID();
        if (uuid == null) {
            return isOp();
        }

        // Check permission manager first
        if (PermissionManager.getInstance().hasPermission(uuid, permission)) {
            return true;
        }

        // Fallback to operator status
        return isOp();
    }

    /**
     * Adds a permission to this player.
     * 
     * @param permission the permission to add
     * @since 1.0.3
     */
    public void addPermission(Permission permission) {
        UUID uuid = getUUID();
        if (uuid != null) {
            PermissionManager.getInstance().addPermission(uuid, permission);
        }
    }

    /**
     * Adds a permission to this player.
     * 
     * @param permission the permission node to add
     * @since 1.0.3
     */
    public void addPermission(String permission) {
        addPermission(Permission.of(permission));
    }

    /**
     * Removes a permission from this player.
     * 
     * @param permission the permission to remove
     * @return true if the permission was removed
     * @since 1.0.3
     */
    public boolean removePermission(Permission permission) {
        UUID uuid = getUUID();
        if (uuid != null) {
            return PermissionManager.getInstance().removePermission(uuid, permission);
        }
        return false;
    }

    /**
     * Removes a permission from this player.
     * 
     * @param permission the permission node to remove
     * @return true if the permission was removed
     * @since 1.0.3
     */
    public boolean removePermission(String permission) {
        return removePermission(Permission.of(permission));
    }

    /**
     * Gets all permissions this player has.
     * 
     * @return an unmodifiable set of permissions
     * @since 1.0.3
     */
    public Set<Permission> getPermissions() {
        UUID uuid = getUUID();
        if (uuid != null) {
            return PermissionManager.getInstance().getPermissions(uuid);
        }
        return Collections.emptySet();
    }

    /**
     * Clears all permissions from this player.
     * 
     * @since 1.0.3
     */
    public void clearPermissions() {
        UUID uuid = getUUID();
        if (uuid != null) {
            PermissionManager.getInstance().clearPermissions(uuid);
        }
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
                true);
    }

    /**
     * Show to the player a title at the top of the screen
     *
     *
     * @param title    the title you want to show to the player
     * @param subtitle the subtitle you want to show to the player
     **/
    public void sendTitleWithSubtitle(String title, String subtitle) {
        EventTitleUtil.showEventTitleToPlayer(
                this.playerRef,
                Message.raw(title),
                Message.raw(subtitle),
                true);
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
