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

    @Override
    public int hashCode() {
        return nativeWorld != null ? nativeWorld.hashCode() : 0;
    }
}
