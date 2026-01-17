package fr.hytale.loader.config;

import java.util.Map;
import java.util.Set;

/**
 * Represents a section within a configuration.
 * <p>
 * ConfigSection allows access to nested configuration values.
 * </p>
 * 
 * <p>
 * <b>Example Usage:</b>
 * </p>
 * 
 * <pre>{@code
 * ConfigSection server = config.getSection("server");
 * String name = server.getString("name");
 * int port = server.getInt("port", 25565);
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 */
public interface ConfigSection {

    /**
     * Gets a value from this section.
     * 
     * @param path the path relative to this section
     * @return the value, or null if not found
     */
    Object get(String path);

    /**
     * Gets a value with default.
     * 
     * @param path the path relative to this section
     * @param def  the default value
     * @return the value, or default if not found
     */
    Object get(String path, Object def);

    /**
     * Gets a string value.
     * 
     * @param path the path relative to this section
     * @return the string value, or null if not found
     */
    String getString(String path);

    /**
     * Gets a string value with default.
     * 
     * @param path the path relative to this section
     * @param def  the default value
     * @return the string value, or default if not found
     */
    String getString(String path, String def);

    /**
     * Gets an integer value.
     * 
     * @param path the path relative to this section
     * @return the integer value, or 0 if not found
     */
    int getInt(String path);

    /**
     * Gets an integer value with default.
     * 
     * @param path the path relative to this section
     * @param def  the default value
     * @return the integer value, or default if not found
     */
    int getInt(String path, int def);

    /**
     * Gets a double value.
     * 
     * @param path the path relative to this section
     * @return the double value, or 0.0 if not found
     */
    double getDouble(String path);

    /**
     * Gets a double value with default.
     * 
     * @param path the path relative to this section
     * @param def  the default value
     * @return the double value, or default if not found
     */
    double getDouble(String path, double def);

    /**
     * Gets a boolean value.
     * 
     * @param path the path relative to this section
     * @return the boolean value, or false if not found
     */
    boolean getBoolean(String path);

    /**
     * Gets a boolean value with default.
     * 
     * @param path the path relative to this section
     * @param def  the default value
     * @return the boolean value, or default if not found
     */
    boolean getBoolean(String path, boolean def);

    /**
     * Gets a nested section.
     * 
     * @param path the path to the section
     * @return the config section, or null if not found
     */
    ConfigSection getSection(String path);

    /**
     * Gets all keys in this section.
     * 
     * @param deep whether to include nested keys
     * @return set of keys
     */
    Set<String> getKeys(boolean deep);

    /**
     * Gets all values in this section.
     * 
     * @return map of all values
     */
    Map<String, Object> getValues();

    /**
     * Checks if a path exists in this section.
     * 
     * @param path the path to check
     * @return true if exists
     */
    boolean contains(String path);
}
