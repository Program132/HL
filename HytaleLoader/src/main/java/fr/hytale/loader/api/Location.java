package fr.hytale.loader.api;

/**
 * Represents a 3D location in a Hytale world.
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.4
 */
public class Location {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private World world;

    /**
     * Constructs a new Location.
     * 
     * @param world the world
     * @param x     the X coordinate
     * @param y     the Y coordinate
     * @param z     the Z coordinate
     */
    public Location(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    /**
     * Constructs a new Location with rotation.
     * 
     * @param world the world
     * @param x     the X coordinate
     * @param y     the Y coordinate
     * @param z     the Z coordinate
     * @param yaw   the yaw rotation
     * @param pitch the pitch rotation
     */
    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Constructs from native world.
     * 
     * @param nativeWorld the native world
     * @param x           the X coordinate
     * @param y           the Y coordinate
     * @param z           the Z coordinate
     */
    public Location(com.hypixel.hytale.server.core.universe.world.World nativeWorld, double x, double y, double z) {
        this(nativeWorld != null ? new World(nativeWorld) : null, x, y, z, 0, 0);
    }

    /**
     * Constructs from native world with rotation.
     * 
     * @param nativeWorld the native world
     * @param x           the X coordinate
     * @param y           the Y coordinate
     * @param z           the Z coordinate
     * @param yaw         the yaw rotation
     * @param pitch       the pitch rotation
     */
    public Location(com.hypixel.hytale.server.core.universe.world.World nativeWorld, double x, double y, double z,
            float yaw, float pitch) {
        this(nativeWorld != null ? new World(nativeWorld) : null, x, y, z, yaw, pitch);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public double distance(Location other) {
        if (other == null || (world != null && !world.equals(other.world))) {
            return Double.MAX_VALUE;
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceSquared(Location other) {
        if (other == null || (world != null && !world.equals(other.world))) {
            return Double.MAX_VALUE;
        }
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public Location clone() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "Location{world=" + (world != null ? world.getName() : "null") +
                ", x=" + x + ", y=" + y + ", z=" + z +
                ", yaw=" + yaw + ", pitch=" + pitch + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Location))
            return false;
        Location other = (Location) obj;
        return Double.compare(other.x, x) == 0 &&
                Double.compare(other.y, y) == 0 &&
                Double.compare(other.z, z) == 0 &&
                Float.compare(other.yaw, yaw) == 0 &&
                Float.compare(other.pitch, pitch) == 0 &&
                (world != null ? world.equals(other.world) : other.world == null);
    }

    @Override
    public int hashCode() {
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Float.floatToIntBits(yaw);
        result = 31 * result + Float.floatToIntBits(pitch);
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }
}
