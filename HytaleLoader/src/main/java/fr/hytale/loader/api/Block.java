package fr.hytale.loader.api;

/**
 * Represents a block in the world.
 * <p>
 * This class acts as a wrapper around coordinates and a world reference.
 * It allows object-oriented manipulation of blocks.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.5
 */
public class Block {

    private final World world;
    private final int x;
    private final int y;
    private final int z;

    /**
     * Creates a new Block reference.
     * 
     * @param world The world containing the block
     * @param x     The X coordinate
     * @param y     The Y coordinate
     * @param z     The Z coordinate
     */
    public Block(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets the world this block is in.
     * 
     * @return The world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the X coordinate.
     * 
     * @return The X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate.
     * 
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the Z coordinate.
     * 
     * @return The Z coordinate
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the location of this block.
     * 
     * @return A new Location object representing this block's position
     */
    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    /**
     * Gets the identifier (type) of this block.
     * 
     * @return The block identifier string (e.g. "Rock_Magma_Cooled") or "Empty"
     */
    public String getType() {
        return world.getBlockIdentifier(x, y, z);
    }

    /**
     * Sets the type of this block.
     * 
     * @param identifier The new block identifier (e.g. "Rock_Magma_Cooled")
     */
    public void setType(String identifier) {
        world.setBlock(x, y, z, identifier);
    }

    @Override
    public String toString() {
        return "Block{world=" + world.getName() + ", x=" + x + ", y=" + y + ", z=" + z + ", type=" + getType() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Block))
            return false;
        Block other = (Block) obj;
        return x == other.x && y == other.y && z == other.z && world.equals(other.world);
    }

    @Override
    public int hashCode() {
        int result = world.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
