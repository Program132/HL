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

    // === Particle API ===

    /**
     * Plays a particle effect at a specific location for all nearby players.
     *
     * @param location     The location to play the particle at
     * @param particleName The particle identifier (e.g. "lx_sparkle_01")
     */
    public void playParticle(Location location, String particleName) {
        if (location == null || particleName == null || nativeWorld == null)
            return;

        if (!location.getWorld().equals(this))
            return;

        nativeWorld.execute(() -> {
            com.hypixel.hytale.math.vector.Vector3d pos = new com.hypixel.hytale.math.vector.Vector3d(
                    location.getX(), location.getY(), location.getZ());

            com.hypixel.hytale.server.core.universe.world.ParticleUtil.spawnParticleEffect(
                    particleName,
                    pos,
                    (com.hypixel.hytale.component.ComponentAccessor) nativeWorld.getEntityStore().getStore());
        });
    }

    // === Weather API ===

    /**
     * Sets the weather for this world.
     *
     * @param weatherName The name of the weather asset (e.g. "sunny", "rain",
     *                    "storm")
     *                    Pass null to reset to dynamic weather.
     */
    public void setWeather(WeatherType weather) {
        setWeather(weather != null ? weather.getAssetName() : null);
    }

    /**
     * Sets the weather for this world.
     *
     * @param weatherName The name of the weather asset (e.g. "Zone1_Sunny",
     *                    "Zone1_Rain").
     *                    Pass null to reset to dynamic weather.
     */
    public void setWeather(String weatherName) {
        if (nativeWorld == null)
            return;

        nativeWorld.execute(() -> {
            com.hypixel.hytale.server.core.universe.world.WorldConfig config = nativeWorld.getWorldConfig();
            config.setForcedWeather(weatherName);
            config.markChanged();

            // Also update WeatherResource to ensure immediate effect/logic consistency
            try {
                com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> accessor = (com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore>) nativeWorld
                        .getEntityStore().getStore();

                com.hypixel.hytale.builtin.weather.resources.WeatherResource weatherResource = (com.hypixel.hytale.builtin.weather.resources.WeatherResource) accessor
                        .getResource(
                                com.hypixel.hytale.builtin.weather.resources.WeatherResource.getResourceType());

                if (weatherResource != null) {
                    weatherResource.setForcedWeather(weatherName);
                }
            } catch (Exception e) {
                // Ignore if WeatherResource is not available or casting fails
            }
        });
    }

    /**
     * Gets the current forced weather name for this world.
     *
     * @return The weather name, or null if dynamic weather is active.
     */
    public String getWeatherName() {
        if (nativeWorld == null)
            return null;
        return nativeWorld.getWorldConfig().getForcedWeather();
    }

    /**
     * Gets the current forced weather type for this world.
     *
     * @return The WeatherType, or null if dynamic weather is active or the type is
     *         unknown to the API.
     */
    public WeatherType getWeather() {
        String name = getWeatherName();
        if (name == null)
            return null;
        return WeatherType.fromAssetName(name);
    }

    // === Time API ===

    /**
     * Sets the time of day using a convenient enum.
     *
     * @param time The time preset to set (e.g. Time.NOON)
     */
    public void setTime(Time time) {
        if (time != null) {
            setTime(time.getPercent());
        }
    }

    /**
     * Sets the time of day as a percentage (0.0 to 1.0).
     * 0.0 is Midnight, 0.5 is Noon.
     *
     * @param percent The percentage of the day passed
     */
    public void setTime(float percent) {
        if (nativeWorld == null)
            return;

        nativeWorld.execute(() -> {
            try {
                // Update Resource
                com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> accessor = (com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore>) nativeWorld
                        .getEntityStore().getStore();

                com.hypixel.hytale.server.core.modules.time.WorldTimeResource timeResource = (com.hypixel.hytale.server.core.modules.time.WorldTimeResource) accessor
                        .getResource(
                                com.hypixel.hytale.server.core.modules.time.WorldTimeResource.getResourceType());

                if (timeResource != null) {
                    timeResource.setDayTime(percent, nativeWorld, nativeWorld.getEntityStore().getStore());
                }

                // Update WorldConfig to keep getters in sync
                long nanosPerDay = java.time.temporal.ChronoUnit.DAYS.getDuration().toNanos();
                java.time.Instant oldGameTime = nativeWorld.getWorldConfig().getGameTime();
                java.time.Instant dayStart = oldGameTime.truncatedTo(java.time.temporal.ChronoUnit.DAYS);
                java.time.Instant newGameTime = dayStart.plusNanos((long) (percent * nanosPerDay));

                // Ensure we move forward in time if the new time is earlier in the day than
                // current
                if (newGameTime.isBefore(oldGameTime)) {
                    newGameTime = newGameTime.plus(1L, java.time.temporal.ChronoUnit.DAYS);
                }

                nativeWorld.getWorldConfig().setGameTime(newGameTime);
                nativeWorld.getWorldConfig().markChanged();

            } catch (Exception e) {
                // Ignore failure
            }
        });
    }

    /**
     * Sets whether the daylight cycle is paused.
     *
     * @param paused true to pause time, false to resume
     */
    public void setTimePaused(boolean paused) {
        if (nativeWorld == null)
            return;

        nativeWorld.execute(() -> {
            nativeWorld.getWorldConfig().setGameTimePaused(paused);
            nativeWorld.getWorldConfig().markChanged();

            // Force update via resource if possible to send packets immediately
            try {
                com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore> accessor = (com.hypixel.hytale.component.ComponentAccessor<com.hypixel.hytale.server.core.universe.world.storage.EntityStore>) nativeWorld
                        .getEntityStore().getStore();

                com.hypixel.hytale.server.core.modules.time.WorldTimeResource timeResource = (com.hypixel.hytale.server.core.modules.time.WorldTimeResource) accessor
                        .getResource(
                                com.hypixel.hytale.server.core.modules.time.WorldTimeResource.getResourceType());

                if (timeResource != null) {
                    timeResource.broadcastTimePacket(nativeWorld.getEntityStore().getStore());
                }
            } catch (Exception e) {
                // Ignore
            }
        });
    }

    /**
     * Gets the current hour of the day (0-23).
     *
     * @return the current hour
     */
    public int getTimeHour() {
        if (nativeWorld == null)
            return 0;
        return nativeWorld.getWorldConfig().getGameTime().atZone(java.time.ZoneOffset.UTC).getHour();
    }

    /**
     * Checks if it is currently day time (between 06:00 and 18:00).
     *
     * @return true if it is day, false if it is night.
     */
    public boolean isDay() {
        int hour = getTimeHour();
        return hour >= 6 && hour < 18;
    }
}
