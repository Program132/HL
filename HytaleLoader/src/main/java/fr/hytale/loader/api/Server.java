package fr.hytale.loader.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Server utility class for server-wide operations.
 * Provides access to online players, broadcasting, and world management.
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 */
public class Server {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private Server() {
        throw new UnsupportedOperationException("Cannot instantiate Server utility class");
    }

    /**
     * Creates a Player wrapper from a PlayerRef.
     * 
     * @param ref The PlayerRef to convert
     * @return A Player instance, or null if the player doesn't have a valid entity
     *         reference
     */
    private static Player fromPlayerRef(PlayerRef ref) {
        if (ref == null) {
            return null;
        }

        Ref<EntityStore> entityRef = ref.getReference();
        if (entityRef == null || !entityRef.isValid()) {
            return null;
        }

        // Components must be accessed from the world thread
        EntityStore entityStore = (EntityStore) entityRef.getStore().getExternalData();
        if (entityStore == null) {
            return null;
        }

        com.hypixel.hytale.server.core.universe.world.World world = entityStore.getWorld();
        if (world == null) {
            return null;
        }

        final com.hypixel.hytale.server.core.entity.entities.Player[] result = new com.hypixel.hytale.server.core.entity.entities.Player[1];
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);

        world.execute(() -> {
            try {
                result[0] = (com.hypixel.hytale.server.core.entity.entities.Player) entityRef
                        .getStore()
                        .getComponent(entityRef,
                                com.hypixel.hytale.server.core.entity.entities.Player.getComponentType());
            } finally {
                latch.countDown();
            }
        });

        try {
            latch.await(1, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        if (result[0] == null) {
            return null;
        }

        return new Player(result[0], ref);
    }

    /**
     * Gets all online players on the server.
     * 
     * @return List of all online Player instances
     */
    public static List<Player> getOnlinePlayers() {
        return Universe.get().getPlayers().stream()
                .map(Server::fromPlayerRef)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Gets a player by their UUID.
     * 
     * @param uuid The UUID of the player to find
     * @return The Player instance, or null if not found or offline
     */
    public static Player getPlayer(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        PlayerRef ref = Universe.get().getPlayer(uuid);
        return fromPlayerRef(ref);
    }

    /**
     * Gets a player by their username (case-insensitive).
     * 
     * @param name The username of the player to find
     * @return The Player instance, or null if not found
     */
    public static Player getPlayer(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return getOnlinePlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Broadcasts a message to all online players.
     * 
     * @param message The message to broadcast
     */
    public static void broadcast(String message) {
        if (message == null) {
            return;
        }

        getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    /**
     * Broadcasts a message to all online players with a specific permission.
     * 
     * @param message    The message to broadcast
     * @param permission The permission required to receive the message
     */
    public static void broadcastPermission(String message, String permission) {
        if (message == null || permission == null) {
            return;
        }

        getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(permission))
                .forEach(p -> p.sendMessage(message));
    }

    /**
     * Gets the number of players currently online.
     * 
     * @return The online player count
     */
    public static int getOnlineCount() {
        return Universe.get().getPlayerCount();
    }

    /**
     * Gets all loaded worlds on the server.
     * 
     * @return List of all loaded World instances
     */
    public static List<World> getWorlds() {
        return Universe.get().getWorlds().values().stream()
                .map(World::new)
                .collect(Collectors.toList());
    }

    /**
     * Gets a world by its name.
     * 
     * @param name The name of the world
     * @return The World instance, or null if not found
     */
    public static World getWorld(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        com.hypixel.hytale.server.core.universe.world.World nativeWorld = Universe.get().getWorld(name);
        return nativeWorld != null ? new World(nativeWorld) : null;
    }

    /**
     * Gets the default world of the server.
     * 
     * @return The default World instance
     */
    public static World getDefaultWorld() {
        com.hypixel.hytale.server.core.universe.world.World nativeWorld = Universe.get().getDefaultWorld();
        return nativeWorld != null ? new World(nativeWorld) : null;
    }

    /**
     * Gets all players in a specific world.
     * 
     * @param world The world to get players from
     * @return List of players in the specified world
     */
    public static List<Player> getPlayersInWorld(World world) {
        if (world == null || world.getNative() == null) {
            return List.of();
        }

        // Use deprecated method warning is expected - native API uses it
        @SuppressWarnings("deprecation")
        List<com.hypixel.hytale.server.core.entity.entities.Player> nativePlayers = world.getNative().getPlayers();

        return nativePlayers.stream()
                .filter(Objects::nonNull)
                .map(nativePlayer -> {
                    // Find the PlayerRef for this native player
                    PlayerRef ref = nativePlayer.getPlayerRef();
                    return (ref != null) ? new Player(nativePlayer, ref) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Gets all players in a world by its name.
     * 
     * @param worldName The name of the world
     * @return List of players in the specified world
     */
    public static List<Player> getPlayersInWorld(String worldName) {
        World world = getWorld(worldName);
        return getPlayersInWorld(world);
    }
}
