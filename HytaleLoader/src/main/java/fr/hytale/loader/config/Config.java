package fr.hytale.loader.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a plugin configuration file.
 * <p>
 * Config provides methods to read and write configuration values with type
 * safety.
 * Supports nested sections, lists, and various data types.
 * </p>
 *
 * <p>
 * <b>Example Usage:</b>
 * </p>
 * 
 * <pre>{@code
 * Config config = plugin.getConfig();
 * 
 * // Get values with defaults
 * String name = config.getString("server.name", "Default Server");
 * int maxPlayers = config.getInt("server.max-players", 20);
 * 
 * // Set values
 * config.set("server.name", "My Server");
 * config.set("server.enabled", true);
 * 
 * // Save changes
 * config.save();
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.4
 */
public interface Config {

    /**
     * Gets a value from the config.
     * 
     * @param path the path to the value (e.g., "server.name")
     * @return the value, or null if not found
     */
    Object get(String path);

    /**
     * Gets a value with a default fallback.
     * 
     * @param path the path to the value
     * @param def  the default value if not found
     * @return the value, or default if not found
     */
    Object get(String path, Object def);

    /**
     * Sets a value in the config.
     * 
     * @param path  the path to set
     * @param value the value to set
     */
    void set(String path, Object value);

    /**
     * Gets a string value.
     * 
     * @param path the path to the value
     * @return the string value, or null if not found
     */
    String getString(String path);

    /**
     * Gets a string value with default.
     * 
     * @param path the path to the value
     * @param def  the default value
     * @return the string value, or default if not found
     */
    String getString(String path, String def);

    /**
     * Gets an integer value.
     * 
     * @param path the path to the value
     * @return the integer value, or 0 if not found
     */
    int getInt(String path);

    /**
     * Gets an integer value with default.
     * 
     * @param path the path to the value
     * @param def  the default value
     * @return the integer value, or default if not found
     */
    int getInt(String path, int def);

    /**
     * Gets a double value.
     * 
     * @param path the path to the value
     * @return the double value, or 0.0 if not found
     */
    double getDouble(String path);

    /**
     * Gets a double value with default.
     * 
     * @param path the path to the value
     * @param def  the default value
     * @return the double value, or default if not found
     */
    double getDouble(String path, double def);

    /**
     * Gets a boolean value.
     * 
     * @param path the path to the value
     * @return the boolean value, or false if not found
     */
    boolean getBoolean(String path);

    /**
     * Gets a boolean value with default.
     * 
     * @param path the path to the value
     * @param def  the default value
     * @return the boolean value, or default if not found
     */
    boolean getBoolean(String path, boolean def);

    /**
     * Gets a list value.
     * 
     * @param path the path to the value
     * @return the list, or null if not found
     */
    List<?> getList(String path);

    /**
     * Gets a list value with default.
     * 
     * @param path the path to the value
     * @param def  the default value
     * @return the list, or default if not found
     */
    List<?> getList(String path, List<?> def);

    /**
     * Gets a string list.
     * 
     * @param path the path to the value
     * @return the string list, or empty list if not found
     */
    List<String> getStringList(String path);

    /**
     * Gets a config section.
     * 
     * @param path the path to the section
     * @return the config section, or null if not found
     */
    ConfigSection getSection(String path);

    /**
     * Checks if a path exists in the config.
     * 
     * @param path the path to check
     * @return true if the path exists
     */
    boolean contains(String path);

    /**
     * Gets all keys in the config.
     * 
     * @param deep whether to get keys from nested sections
     * @return set of all keys
     */
    Set<String> getKeys(boolean deep);

    /**
     * Gets all values as a map.
     * 
     * @return map of all values
     */
    Map<String, Object> getValues();

    /**
     * Saves the config to file.
     * 
     * @throws IOException if save fails
     */
    void save() throws IOException;

    /**
     * Reloads the config from file.
     * 
     * @throws IOException if reload fails
     */
    void reload() throws IOException;

    /**
     * Gets the file this config is bound to.
     * 
     * @return the config file
     */
    File getFile();

    /**
     * Sets the default values for this config.
     * Values will be used if not present in the file.
     * 
     * @param defaults map of default values
     */
    void setDefaults(Map<String, Object> defaults);

    /**
     * Adds default values to the config.
     * 
     * @param path  the path to set
     * @param value the default value
     */
    void addDefault(String path, Object value);
}
