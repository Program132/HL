package fr.hytale.loader.api;

/**
 * Represents the different weather types available in Hytale.
 * <p>
 * This enum wraps the native weather asset IDs used by the game.
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public enum WeatherType {

    // Zone 1 (Emerald Grove)
    CLEAR("Zone1_Sunny"),
    CLOUDY("Zone1_Cloudy_Medium"),
    RAIN("Zone1_Rain"),
    LIGHT_RAIN("Zone1_Rain_Light"),
    STORM("Zone1_Storm"),
    FOGGY("Zone1_Foggy_Light"),

    // Zone 1 Specific
    AUTUMN_WINDY("Zone1_Autumn_Forest_Windy"),
    FIREFLIES("Zone1_Azurewood_Fireflies"),
    SWAMP("Zone1_Swamp"),
    SWAMP_FOGGY("Zone1_Swamp_Foggy"),
    SWAMP_RAIN("Zone1_Swamp_Rain"),

    // Zone 2 (Howling Sands)
    SANDSTORM("Zone2_Sandstorm"),

    // Caves
    CAVE_SHALLOW("Cave_Shallow"),
    CAVE_DEEP("Cave_Deep"),
    CAVE_FOG("Cave_Fog"),
    CAVE_MAGMA("Cave_Volcanic"),

    // Special
    BLOOD_MOON("Blood_Moon"),
    VOID("Void");

    private final String assetName;

    WeatherType(String assetName) {
        this.assetName = assetName;
    }

    /**
     * Gets the native asset name for this weather type.
     *
     * @return the asset name (e.g. "Zone1_Sunny")
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Gets a WeatherType from its asset name.
     *
     * @param assetName the asset name
     * @return the WeatherType, or null if not found
     */
    public static WeatherType fromAssetName(String assetName) {
        for (WeatherType type : values()) {
            if (type.assetName.equalsIgnoreCase(assetName)) {
                return type;
            }
        }
        return null;
    }
}
