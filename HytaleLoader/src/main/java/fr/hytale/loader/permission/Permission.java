package fr.hytale.loader.permission;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Represents a permission node in HytaleLoader.
 * <p>
 * Permissions are used to control access to commands and features.
 * This class provides a wrapper around Hytale's native String-based permission
 * system.
 * </p>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <pre>{@code
 * // Create a permission
 * Permission adminPerm = Permission.of("myplugin.admin");
 * 
 * // Check if player has permission
 * if (player.hasPermission(adminPerm)) {
 *     // Player has admin permission
 * }
 * 
 * // Create from native Hytale permission
 * Permission cmdPerm = Permission.fromNative(HytalePermissions.fromCommand("gamemode"));
 * 
 * // Convert to native
 * String nativePerm = adminPerm.toNative();
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.3
 */
public class Permission {

    private final String node;

    /**
     * Creates a new permission with the specified node.
     * 
     * @param node the permission node (e.g., "myplugin.admin")
     */
    private Permission(@Nonnull String node) {
        this.node = Objects.requireNonNull(node, "Permission node cannot be null");
    }

    /**
     * Creates a permission from a permission node string.
     * 
     * @param node the permission node
     * @return a new Permission instance
     */
    @Nonnull
    public static Permission of(@Nonnull String node) {
        return new Permission(node);
    }

    /**
     * Creates a permission from a native Hytale permission string.
     * 
     * @param nativePermission the native Hytale permission
     * @return a new Permission instance
     */
    @Nonnull
    public static Permission fromNative(@Nonnull String nativePermission) {
        return new Permission(nativePermission);
    }

    /**
     * Creates a command permission.
     * <p>
     * This follows the Hytale convention of "namespace.command.commandname".
     * </p>
     * 
     * @param namespace   the plugin namespace (e.g., "myplugin")
     * @param commandName the command name
     * @return a new Permission instance
     */
    @Nonnull
    public static Permission forCommand(@Nonnull String namespace, @Nonnull String commandName) {
        return new Permission(namespace + ".command." + commandName);
    }

    /**
     * Creates a command permission with a sub-command.
     * 
     * @param namespace   the plugin namespace
     * @param commandName the command name
     * @param subCommand  the sub-command name
     * @return a new Permission instance
     */
    @Nonnull
    public static Permission forCommand(@Nonnull String namespace, @Nonnull String commandName,
            @Nonnull String subCommand) {
        return new Permission(namespace + ".command." + commandName + "." + subCommand);
    }

    /**
     * Creates a Hytale native command permission.
     * <p>
     * Uses the "hytale.command.*" namespace.
     * </p>
     * 
     * @param commandName the command name
     * @return a new Permission instance
     */
    @Nonnull
    public static Permission hytaleCommand(@Nonnull String commandName) {
        return fromNative(com.hypixel.hytale.server.core.permissions.HytalePermissions.fromCommand(commandName));
    }

    /**
     * Converts this permission to a native Hytale permission string.
     * 
     * @return the permission node as a string
     */
    @Nonnull
    public String toNative() {
        return this.getNode();
    }

    /**
     * Gets the permission node.
     * 
     * @return the permission node string
     */
    @Nonnull
    public String getNode() {
        return node;
    }

    /**
     * Checks if this permission is a child of another permission.
     * <p>
     * For example, "myplugin.admin.edit" is a child of "myplugin.admin".
     * </p>
     * 
     * @param parent the potential parent permission
     * @return true if this permission is a child of the parent
     */
    public boolean isChildOf(@Nonnull Permission parent) {
        return node.startsWith(parent.node + ".");
    }

    /**
     * Checks if this permission is a parent of another permission.
     * 
     * @param child the potential child permission
     * @return true if this permission is a parent of the child
     */
    public boolean isParentOf(@Nonnull Permission child) {
        return child.isChildOf(this);
    }

    /**
     * Gets the parent permission of this permission.
     * <p>
     * For example, the parent of "myplugin.admin.edit" is "myplugin.admin".
     * Returns null if this permission has no parent (only one segment).
     * </p>
     * 
     * @return the parent permission, or null if no parent exists
     */
    public Permission getParent() {
        int lastDot = node.lastIndexOf('.');
        if (lastDot == -1) {
            return null;
        }
        return Permission.of(node.substring(0, lastDot));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Permission))
            return false;
        Permission other = (Permission) obj;
        return node.equals(other.node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public String toString() {
        return node;
    }
}
