package fr.hytale.loader.permission;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central manager for player permissions in HytaleLoader.
 * <p>
 * This singleton class stores and manages permissions for all players.
 * Permissions are stored by player UUID to persist across Player instance
 * creations.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.3
 */
public class PermissionManager {

    private static final PermissionManager INSTANCE = new PermissionManager();

    private final Map<UUID, Set<Permission>> playerPermissions;

    private PermissionManager() {
        this.playerPermissions = new ConcurrentHashMap<>();
    }

    /**
     * Gets the singleton instance of the PermissionManager.
     * 
     * @return the PermissionManager instance
     */
    @Nonnull
    public static PermissionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Checks if a player has a specific permission.
     * 
     * @param playerUUID the player's UUID
     * @param permission the permission to check
     * @return true if the player has the permission
     */
    public boolean hasPermission(@Nonnull UUID playerUUID, @Nonnull Permission permission) {
        Set<Permission> permissions = playerPermissions.get(playerUUID);
        if (permissions == null) {
            return false;
        }

        // Check exact permission
        if (permissions.contains(permission)) {
            return true;
        }

        // Check parent permissions (wildcard support)
        Permission parent = permission.getParent();
        while (parent != null) {
            if (permissions.contains(parent)) {
                return true;
            }
            parent = parent.getParent();
        }

        // Check for wildcard
        return permissions.contains(Permission.of("*"));
    }

    /**
     * Adds a permission to a player.
     * 
     * @param playerUUID the player's UUID
     * @param permission the permission to add
     */
    public void addPermission(@Nonnull UUID playerUUID, @Nonnull Permission permission) {
        playerPermissions.computeIfAbsent(playerUUID, k -> ConcurrentHashMap.newKeySet()).add(permission);
    }

    /**
     * Removes a permission from a player.
     * 
     * @param playerUUID the player's UUID
     * @param permission the permission to remove
     * @return true if the permission was removed
     */
    public boolean removePermission(@Nonnull UUID playerUUID, @Nonnull Permission permission) {
        Set<Permission> permissions = playerPermissions.get(playerUUID);
        if (permissions == null) {
            return false;
        }
        return permissions.remove(permission);
    }

    /**
     * Gets all permissions for a player.
     * 
     * @param playerUUID the player's UUID
     * @return an unmodifiable set of permissions
     */
    @Nonnull
    public Set<Permission> getPermissions(@Nonnull UUID playerUUID) {
        Set<Permission> permissions = playerPermissions.get(playerUUID);
        if (permissions == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * Clears all permissions for a player.
     * 
     * @param playerUUID the player's UUID
     */
    public void clearPermissions(@Nonnull UUID playerUUID) {
        playerPermissions.remove(playerUUID);
    }

    /**
     * Clears all permissions for all players.
     * <p>
     * Use with caution - this will remove all stored permissions.
     * </p>
     */
    public void clearAll() {
        playerPermissions.clear();
    }

    /**
     * Gets the number of players with stored permissions.
     * 
     * @return the number of players with permissions
     */
    public int getPlayerCount() {
        return playerPermissions.size();
    }
}
