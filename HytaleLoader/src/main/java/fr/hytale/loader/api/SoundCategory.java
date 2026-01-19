package fr.hytale.loader.api;

/**
 * Represents the category of a sound.
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public enum SoundCategory {
    MASTER,
    MUSIC,
    RECORD,
    WEATHER,
    BLOCK,
    HOSTILE,
    NEUTRAL,
    PLAYER,
    AMBIENT,
    VOICE,
    SFX;

    /**
     * Converts to the native Hytale SoundCategory.
     * 
     * @return the native SoundCategory
     */
    public com.hypixel.hytale.protocol.SoundCategory toNative() {
        try {
            return com.hypixel.hytale.protocol.SoundCategory.valueOf(this.name());
        } catch (IllegalArgumentException e) {
            // Default to SFX if not found
            return com.hypixel.hytale.protocol.SoundCategory.SFX;
        }
    }
}
