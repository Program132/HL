package fr.hytale.loader.api;

/**
 * Represents the game mode of a player.
 * <p>
 * This enum provides a simplified wrapper around Hytale's native GameMode
 * class,
 * making it easier to work with game modes without directly depending on the
 * native API.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.2
 */
public enum GameMode {

    /**
     * Creative mode - players have unlimited resources and cannot take damage.
     */
    CREATIVE(0),

    /**
     * Adventure mode - default mode for players, similar to survival from Minecraft
     */
    ADVENTURE(1);

    private final int id;

    /**
     * Constructs a GameMode with the specified ID.
     * 
     * @param id the numeric ID of this game mode
     */
    GameMode(int id) {
        this.id = id;
    }

    /**
     * Gets the numeric ID of this game mode.
     * 
     * @return the game mode ID
     */
    public int getId() {
        return id;
    }

    /**
     * Converts this HytaleLoader GameMode to the native Hytale GameMode.
     * 
     * @return the native Hytale GameMode instance
     */
    public com.hypixel.hytale.protocol.GameMode toNative() {
        return switch (this) {
            case ADVENTURE -> com.hypixel.hytale.protocol.GameMode.Adventure;
            case CREATIVE -> com.hypixel.hytale.protocol.GameMode.Creative;
        };
    }

    /**
     * Converts a native Hytale GameMode to a HytaleLoader GameMode.
     * 
     * @param nativeGameMode the native Hytale game mode
     * @return the HytaleLoader GameMode, or SURVIVAL if null or unknown
     */
    public static GameMode fromNative(com.hypixel.hytale.protocol.GameMode nativeGameMode) {
        if (nativeGameMode == null) {
            return ADVENTURE;
        }

        if (nativeGameMode == com.hypixel.hytale.protocol.GameMode.Creative) {
            return CREATIVE;
        } else if (nativeGameMode == com.hypixel.hytale.protocol.GameMode.Adventure) {
            return ADVENTURE;
        }

        return ADVENTURE; // Default fallback
    }

    /**
     * Gets a GameMode by its numeric ID.
     * 
     * @param id the game mode ID
     * @return the GameMode, or SURVIVAL if the ID is invalid
     */
    public static GameMode fromId(int id) {
        return switch (id) {
            case 0 -> CREATIVE;
            case 1 -> ADVENTURE;
            default -> ADVENTURE;
        };
    }
}
