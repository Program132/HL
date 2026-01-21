package fr.hytale.loader.api;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Represents an entity in the Hytale world.
 * <p>
 * This class wraps a native Hytale entity and provides common methods
 * for interaction, such as location management and removal.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.5
 */
public class Entity {

    protected final com.hypixel.hytale.server.core.entity.Entity nativeEntity;

    /**
     * Creates a new Entity wrapper.
     * 
     * @param nativeEntity The native Hytale entity
     */
    public Entity(com.hypixel.hytale.server.core.entity.Entity nativeEntity) {
        this.nativeEntity = nativeEntity;
    }

    /**
     * Gets the native Hytale entity.
     * 
     * @return The native entity
     */
    public com.hypixel.hytale.server.core.entity.Entity getNativeEntity() {
        return nativeEntity;
    }

    /**
     * Gets the network ID of the entity.
     * 
     * @return The network ID, or -1 if invalid
     */
    public int getID() {
        return nativeEntity != null ? nativeEntity.getNetworkId() : -1;
    }

    /**
     * Gets the UUID of the entity.
     * 
     * @return The UUID, or null if invalid
     */
    public UUID getUUID() {
        return nativeEntity != null ? nativeEntity.getUuid() : null;
    }

    /**
     * Gets the World this entity is currently in.
     * 
     * @return The world, or null if removed/unloaded
     */
    public World getWorld() {
        if (nativeEntity == null || nativeEntity.getWorld() == null) {
            return null;
        }
        return new World(nativeEntity.getWorld());
    }

    /**
     * Removes the entity from the world.
     */
    public void remove() {
        if (nativeEntity != null && !nativeEntity.wasRemoved()) {
            nativeEntity.remove();
        }
    }

    /**
     * Checks if the entity has been removed or is valid.
     * 
     * @return True if valid, false if removed
     */
    public boolean isValid() {
        return nativeEntity != null && !nativeEntity.wasRemoved() && nativeEntity.getWorld() != null;
    }

    /**
     * Gets the entity's current location.
     * 
     * @return The location, or null if unavailable
     */
    public Location getLocation() {
        if (nativeEntity == null)
            return null;

        com.hypixel.hytale.server.core.universe.world.World world = nativeEntity.getWorld();
        if (world == null)
            return null;

        try {
            return getLocationInternal(world);
        } catch (IllegalStateException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Assert not in thread")) {
                final java.util.concurrent.CompletableFuture<Location> future = new java.util.concurrent.CompletableFuture<>();
                world.execute(() -> {
                    try {
                        future.complete(getLocationInternal(world));
                    } catch (Exception ex) {
                        future.completeExceptionally(ex);
                    }
                });
                try {
                    return future.join();
                } catch (Exception ex) {
                    return null;
                }
            }
            throw e;
        }
    }

    private Location getLocationInternal(com.hypixel.hytale.server.core.universe.world.World world) {
        Ref ref = nativeEntity.getReference();
        if (ref != null && ref.isValid()) {
            Store<EntityStore> store = ref.getStore();

            TransformComponent transform = (TransformComponent) store.getComponent(ref,
                    TransformComponent.getComponentType());

            if (transform != null) {
                com.hypixel.hytale.math.vector.Vector3d position = transform.getPosition();
                com.hypixel.hytale.math.vector.Vector3f rotation = transform.getRotation();

                return new Location(
                        new World(world),
                        position.getX(), position.getY(), position.getZ(),
                        rotation.getYaw(), rotation.getPitch());
            }
        }
        return null;
    }

    /**
     * Teleports the entity to a location.
     * 
     * @param location The target location
     */
    public void teleport(Location location) {
        if (location == null || nativeEntity == null)
            return;

        com.hypixel.hytale.server.core.universe.world.World world = nativeEntity.getWorld();
        if (world == null)
            return;

        world.execute(() -> {
            try {
                Ref ref = nativeEntity.getReference();
                if (ref != null && ref.isValid()) {
                    Store<EntityStore> store = ref.getStore();

                    com.hypixel.hytale.math.vector.Vector3d position = new com.hypixel.hytale.math.vector.Vector3d(
                            location.getX(), location.getY(), location.getZ());
                    com.hypixel.hytale.math.vector.Vector3f rotation = new com.hypixel.hytale.math.vector.Vector3f(
                            location.getYaw(), location.getPitch(), 0.0f);
                    com.hypixel.hytale.math.vector.Transform transform = new com.hypixel.hytale.math.vector.Transform(
                            position, rotation);

                    com.hypixel.hytale.server.core.modules.entity.teleport.Teleport teleport = new com.hypixel.hytale.server.core.modules.entity.teleport.Teleport(
                            world, transform.getPosition(), transform.getRotation());
                    store.addComponent(ref,
                            com.hypixel.hytale.server.core.modules.entity.teleport.Teleport.getComponentType(),
                            teleport);
                }
            } catch (Exception e) {
                // Ignore
            }
        });
    }

    /**
     * Teleports the entity to coordinates.
     * 
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public void teleport(double x, double y, double z) {
        if (nativeEntity == null)
            return;
        com.hypixel.hytale.server.core.universe.world.World world = nativeEntity.getWorld();
        if (world != null) {
            teleport(new Location(new World(world), x, y, z));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Entity))
            return false;
        Entity other = (Entity) obj;
        return getID() == other.getID();
    }

    @Override
    public int hashCode() {
        return getID();
    }
}
