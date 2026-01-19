package fr.hytale.loader.api;

/**
 * Represents a Hytale world.
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.4
 */
public class World {

    private final com.hypixel.hytale.server.core.universe.world.World nativeWorld;

    /**
     * Constructs a new World wrapper.
     * 
     * @param nativeWorld the native Hytale world
     */
    public World(com.hypixel.hytale.server.core.universe.world.World nativeWorld) {
        this.nativeWorld = nativeWorld;
    }

    /**
     * Gets the native Hytale world object.
     * 
     * @return the native world
     */
    public com.hypixel.hytale.server.core.universe.world.World getNative() {
        return nativeWorld;
    }

    /**
     * Gets the world name.
     * 
     * @return the world name
     */
    public String getName() {
        return nativeWorld != null ? nativeWorld.getName() : "unknown";
    }

    @Override
    public String toString() {
        return "World{name=" + getName() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof World))
            return false;
        World other = (World) obj;
        return nativeWorld != null && nativeWorld.equals(other.nativeWorld);
    }

    /**
     * Gets the identifier of the block at the specified coordinates.
     * 
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return The block identifier (e.g., "hytale:stone"), or "hytale:air" if not
     *         found
     */
    public String getBlockIdentifier(int x, int y, int z) {
        if (nativeWorld == null)
            return "Empty";

        long chunkIndex = com.hypixel.hytale.math.util.ChunkUtil.indexChunkFromBlock(x, z);
        com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk chunk = nativeWorld.getChunk(chunkIndex);

        if (chunk != null) {
            com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType type = chunk.getBlockType(x, y, z);
            return type != null ? type.getId() : "Empty";
        }
        return "Empty";
    }

    /**
     * Gets the identifier of the block at the specified location.
     * 
     * @param location The location to check
     * @return The block identifier
     */
    public String getBlockIdentifier(Location location) {
        if (location == null)
            return "Empty";
        return getBlockIdentifier((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    /**
     * Sets the block at the specified coordinates.
     * 
     * @param x       The X coordinate
     * @param y       The Y coordinate
     * @param z       The Z coordinate
     * @param blockId The block identifier (e.g., "hytale:stone")
     */
    public void setBlock(int x, int y, int z, String blockId) {
        if (nativeWorld == null)
            return;

        long chunkIndex = com.hypixel.hytale.math.util.ChunkUtil.indexChunkFromBlock(x, z);
        com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk chunk = nativeWorld.getChunk(chunkIndex);

        if (chunk != null) {
            chunk.setBlock(x, y, z, blockId);
        }
    }

    /**
     * Sets the block at the specified location.
     * 
     * @param location The location where to set the block
     * @param blockId  The block identifier
     */
    public void setBlock(Location location, String blockId) {
        if (location == null)
            return;
        setBlock((int) location.getX(), (int) location.getY(), (int) location.getZ(), blockId);
    }

    /**
     * Sets the block at the specified location in the block arguments.
     * 
     * @param block The block to set
     */
    public void setBlock(Block block) {
        if (block == null)
            return;
        setBlock((int) block.getX(), (int) block.getY(), (int) block.getZ(), block.getType());
    }

    /**
     * Gets the Block object at the specified coordinates.
     * 
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @return The Block object
     */
    public Block getBlockAt(int x, int y, int z) {
        return new Block(this, x, y, z);
    }

    /**
     * Gets the Block object at the specified location.
     * 
     * @param location The location
     * @return The Block object, or null if location is invalid/different world
     */
    public Block getBlockAt(Location location) {
        if (location == null || !location.getWorld().equals(this))
            return null;
        return getBlockAt((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }

    /**
     * Gets an entity by its network ID.
     * 
     * @param id The entity's network ID
     * @return The Entity, or null if not found
     */
    public Entity getEntity(int id) {
        if (nativeWorld == null)
            return null;

        try {
            return getEntityInternal(id);
        } catch (IllegalStateException e) {
            if (e.getMessage() != null && e.getMessage().contains("Assert not in thread")) {
                final java.util.concurrent.CompletableFuture<Entity> future = new java.util.concurrent.CompletableFuture<>();
                nativeWorld.execute(() -> {
                    try {
                        future.complete(getEntityInternal(id));
                    } catch (Exception ex) {
                        future.completeExceptionally(ex);
                    }
                });
                try {
                    return future.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            throw e;
        }
    }

    private Entity getEntityInternal(int id) {
        com.hypixel.hytale.server.core.universe.world.storage.EntityStore entityStore = nativeWorld.getEntityStore();
        com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> ref = entityStore
                .getRefFromNetworkId(id);
        return createEntityFromRef(ref);
    }

    /**
     * Gets an entity by its UUID.
     * 
     * @param uuid The entity's UUID
     * @return The Entity, or null if not found
     */
    public Entity getEntity(java.util.UUID uuid) {
        if (nativeWorld == null || uuid == null)
            return null;

        try {
            return getEntityInternal(uuid);
        } catch (IllegalStateException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Assert not in thread")) {
                final java.util.concurrent.CompletableFuture<Entity> future = new java.util.concurrent.CompletableFuture<>();
                nativeWorld.execute(() -> {
                    try {
                        future.complete(getEntityInternal(uuid));
                    } catch (Exception ex) {
                        future.completeExceptionally(ex);
                    }
                });
                try {
                    return future.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            throw e;
        }
    }

    private Entity getEntityInternal(java.util.UUID uuid) {
        com.hypixel.hytale.server.core.universe.world.storage.EntityStore entityStore = nativeWorld.getEntityStore();
        com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> ref = entityStore
                .getRefFromUUID(uuid);
        return createEntityFromRef(ref);
    }

    /**
     * Spawns an entity at the specified location.
     * 
     * @param location   The location to spawn the entity
     * @param entityType The type of entity (e.g. "Antelope", "Bat_Ice")
     * @return The spawned Entity, or null if failed
     */
    public Entity spawnEntity(Location location, String entityType) {
        if (nativeWorld == null || location == null || entityType == null)
            return null;

        try {
            return spawnEntityInternal(location, entityType);
        } catch (IllegalStateException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Assert not in thread")) {
                final java.util.concurrent.CompletableFuture<Entity> future = new java.util.concurrent.CompletableFuture<>();
                nativeWorld.execute(() -> {
                    try {
                        future.complete(spawnEntityInternal(location, entityType));
                    } catch (Exception ex) {
                        future.completeExceptionally(ex);
                    }
                });
                try {
                    return future.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Entity spawnEntityInternal(Location location, String entityType) {
        com.hypixel.hytale.server.npc.NPCPlugin npcPlugin = com.hypixel.hytale.server.npc.NPCPlugin.get();
        if (npcPlugin == null)
            return null;

        com.hypixel.hytale.math.vector.Vector3d pos = new com.hypixel.hytale.math.vector.Vector3d(location.getX(),
                location.getY(), location.getZ());
        com.hypixel.hytale.math.vector.Vector3f rot = new com.hypixel.hytale.math.vector.Vector3f(0, location.getYaw(),
                location.getPitch());

        com.hypixel.hytale.component.Store<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> store = nativeWorld
                .getEntityStore().getStore();

        it.unimi.dsi.fastutil.Pair<com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore>, com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter> pair = npcPlugin
                .spawnNPC(store, entityType, null, pos, rot);

        if (pair != null && pair.first() != null) {
            return createEntityFromRef(pair.first());
        }
        return null;
    }

    /**
     * Helper to create an API Entity from a Ref.
     */
    private Entity createEntityFromRef(
            com.hypixel.hytale.component.Ref<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> ref) {
        if (ref == null || !ref.isValid())
            return null;

        // Check for Player
        com.hypixel.hytale.component.Store<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> store = ref
                .getStore();

        // Try to get PlayerRef component
        com.hypixel.hytale.server.core.universe.PlayerRef playerRef = (com.hypixel.hytale.server.core.universe.PlayerRef) store
                .getComponent(ref, com.hypixel.hytale.server.core.universe.PlayerRef.getComponentType());

        if (playerRef != null) {
            // It's a player, get the Native Player Entity
            com.hypixel.hytale.server.core.entity.entities.Player nativePlayer = (com.hypixel.hytale.server.core.entity.entities.Player) store
                    .getComponent(ref, com.hypixel.hytale.server.core.entity.entities.Player.getComponentType());
            if (nativePlayer != null) {
                return new Player(nativePlayer, playerRef);
            }
        }

        // It's a generic entity (or NPC). Find the Entity component.
        com.hypixel.hytale.component.Archetype<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> archetype = store
                .getArchetype(ref);
        for (int i = archetype.getMinIndex(); i < archetype.length(); i++) {
            com.hypixel.hytale.component.ComponentType type = archetype.get(i);
            if (type != null
                    && com.hypixel.hytale.server.core.entity.Entity.class.isAssignableFrom(type.getTypeClass())) {
                com.hypixel.hytale.server.core.entity.Entity nativeEntity = (com.hypixel.hytale.server.core.entity.Entity) store
                        .getComponent(ref, type);
                if (nativeEntity != null) {
                    return new Entity(nativeEntity);
                }
            }
        }

        return null;
    }

    @Override
    public int hashCode() {
        return nativeWorld != null ? nativeWorld.hashCode() : 0;
    }

    // === Sound API ===

    /**
     * Plays a sound at a specific location for all nearby players.
     * 
     * @param location The location to play the sound at
     * @param sound    The sound identifier (e.g. "my.sound.effect")
     * @param volume   The volume (1.0 is normal)
     * @param pitch    The pitch (1.0 is normal)
     * @since 1.0.6
     */
    public void playSound(Location location, String sound, float volume, float pitch) {
        playSound(location, sound, SoundCategory.SFX, volume, pitch);
    }

    /**
     * Plays a sound at a specific location for all nearby players.
     * 
     * @param location The location to play the sound at
     * @param sound    The sound identifier
     * @param category The sound category
     * @param volume   The volume
     * @param pitch    The pitch
     * @since 1.0.6
     */
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        if (location == null || sound == null || category == null || nativeWorld == null)
            return;

        if (!location.getWorld().equals(this))
            return;

        int soundIndex = com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent.getAssetMap()
                .getIndex(sound);
        if (soundIndex == 0)
            return;

        nativeWorld.execute(() -> {
            com.hypixel.hytale.server.core.universe.world.SoundUtil.playSoundEvent3d(
                    soundIndex,
                    category.toNative(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    volume,
                    pitch,
                    (com.hypixel.hytale.component.ComponentAccessor) nativeWorld.getEntityStore().getStore());
        });
    }
}
