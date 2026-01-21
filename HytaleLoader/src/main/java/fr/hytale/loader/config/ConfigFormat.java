package fr.hytale.loader.config;

/**
 * Format of the configuration file.
 * <p>
 * Defines the supported file formats for plugin configurations.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.4
 */
public enum ConfigFormat {
    /**
     * YAML format (config.yml).
     * This is the default format used by Bukkit/Spigot plugins and defaults in
     * HytaleLoader.
     */
    YAML("config.yml"),

    /**
     * JSON format (config.json).
     * A structured format widely used in web APIs and configuration.
     */
    JSON("config.json");

    private final String fileName;

    /**
     * Constructs a new ConfigFormat.
     * 
     * @param fileName the default filename associated with this format
     */
    ConfigFormat(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the default file name for this format.
     * 
     * @return the file name (e.g. "config.yml" or "config.json")
     */
    public String getFileName() {
        return fileName;
    }
}
