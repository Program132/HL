package fr.hytale.loader.api;

/**
 * Represents a Hytale world.
 * 
 * @author HytaleLoader
 * @version 1.0.4
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

    @Override
    public int hashCode() {
        return nativeWorld != null ? nativeWorld.hashCode() : 0;
    }
}
