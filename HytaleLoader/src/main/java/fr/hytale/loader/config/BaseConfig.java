package fr.hytale.loader.config;

import java.io.File;
import java.util.*;

/**
 * Base implementation for configuration files.
 * <p>
 * Provides memory management for config data as nested Maps.
 * This class implements the core logic for getting and setting values,
 * handling defaults, and managing nested sections.
 * </p>
 * <p>
 * Concrete implementations only need to handle the actual loading and saving
 * of data from/to their specific file format (e.g., YAML, JSON).
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 */
public abstract class BaseConfig implements Config {

    /** The file associated with this configuration. */
    protected final File file;
    /** The data map holding the configuration values. */
    protected final Map<String, Object> data;
    /** The map holding default values. */
    protected Map<String, Object> defaults;

    /**
     * Creates a new BaseConfig instance.
     * 
     * @param file the configuration file
     */
    public BaseConfig(File file) {
        this.file = file;
        this.data = new LinkedHashMap<>();
        this.defaults = new LinkedHashMap<>();
    }

    /**
     * Gets a value from the config.
     * 
     * @param path the path to the value
     * @return the value, or null if not found
     */
    @Override
    public Object get(String path) {
        return get(path, null);
    }

    /**
     * Gets a value from the config with a default fallback.
     * <p>
     * If the value is not found in the config data, it attempts to retrieve it
     * from the defaults.
     * </p>
     * 
     * @param path the path to the value
     * @param def  the default value to return if not found
     * @return the value, or the default if not found
     */
    @Override
    public Object get(String path, Object def) {
        if (path == null || path.isEmpty()) {
            return def;
        }

        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                return getFromDefaults(path, def);
            }
            current = (Map<String, Object>) next;
        }

        Object value = current.get(parts[parts.length - 1]);
        return value != null ? value : getFromDefaults(path, def);
    }

    /**
     * Helper method to get a value from the defaults map.
     * 
     * @param path the path to the value
     * @param def  the fallback value if not found in defaults
     * @return the default value, or def if not found
     */
    protected Object getFromDefaults(String path, Object def) {
        if (defaults == null) {
            return def;
        }

        String[] parts = path.split("\\.");
        Map<String, Object> current = defaults;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                return def;
            }
            current = (Map<String, Object>) next;
        }

        Object value = current.get(parts[parts.length - 1]);
        return value != null ? value : def;
    }

    /**
     * Sets a value in the configuration at the specified path.
     * <p>
     * Creating any necessary nested sections if they do not exist.
     * </p>
     * 
     * @param path  the path to set
     * @param value the value to set
     */
    @Override
    public void set(String path, Object value) {
        if (path == null || path.isEmpty()) {
            return;
        }

        String[] parts = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                current.put(parts[i], newMap);
                current = newMap;
            } else {
                current = (Map<String, Object>) next;
            }
        }

        current.put(parts[parts.length - 1], value);
    }

    /**
     * Gets a String value from the config.
     * 
     * @param path the path to the value
     * @return the string value, or null
     */
    @Override
    public String getString(String path) {
        return getString(path, null);
    }

    /**
     * Gets a String value from the config with a default.
     * 
     * @param path the path to the value
     * @param def  the default string
     * @return the string value, or default
     */
    @Override
    public String getString(String path, String def) {
        Object value = get(path, def);
        return value != null ? String.valueOf(value) : def;
    }

    /**
     * Gets an int value from the config.
     * 
     * @param path the path to the value
     * @return the int value, or 0
     */
    @Override
    public int getInt(String path) {
        return getInt(path, 0);
    }

    /**
     * Gets an int value from the config with a default.
     * <p>
     * Tries to parse the value as an integer if it is stored as a String.
     * </p>
     * 
     * @param path the path to the value
     * @param def  the default int
     * @return the int value, or default
     */
    @Override
    public int getInt(String path, int def) {
        Object value = get(path, def);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Gets a double value from the config.
     * 
     * @param path the path to the value
     * @return the double value, or 0.0
     */
    @Override
    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }

    /**
     * Gets a double value from the config with a default.
     * <p>
     * Tries to parse the value as a double if it is stored as a String.
     * </p>
     * 
     * @param path the path to the value
     * @param def  the default double
     * @return the double value, or default
     */
    @Override
    public double getDouble(String path, double def) {
        Object value = get(path, def);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Gets a boolean value from the config.
     * 
     * @param path the path to the value
     * @return the boolean value, or false
     */
    @Override
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    /**
     * Gets a boolean value from the config with a default.
     * <p>
     * Tries to parse the value as a boolean if it is stored as a String.
     * </p>
     * 
     * @param path the path to the value
     * @param def  the default boolean
     * @return the boolean value, or default
     */
    @Override
    public boolean getBoolean(String path, boolean def) {
        Object value = get(path, def);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return def;
    }

    /**
     * Gets a List from the config.
     * 
     * @param path the path to the value
     * @return the List, or null
     */
    @Override
    public List<?> getList(String path) {
        return getList(path, null);
    }

    /**
     * Gets a List from the config with a default.
     * 
     * @param path the path to the value
     * @param def  the default List
     * @return the List, or default
     */
    @Override
    public List<?> getList(String path, List<?> def) {
        Object value = get(path, def);
        return value instanceof List ? (List<?>) value : def;
    }

    /**
     * Gets a List of Strings from the config.
     * <p>
     * Converts all elements in the list to String.
     * </p>
     * 
     * @param path the path to the value
     * @return a List of Strings (never null, empty if not found)
     */
    @Override
    public List<String> getStringList(String path) {
        List<?> list = getList(path);
        if (list == null) {
            return new ArrayList<>();
        }

        List<String> result = new ArrayList<>();
        for (Object obj : list) {
            result.add(String.valueOf(obj));
        }
        return result;
    }

    /**
     * Gets a Configuration Section.
     * 
     * @param path the path to the section
     * @return the ConfigSection, or null if not found
     */
    @Override
    public ConfigSection getSection(String path) {
        Object value = get(path);
        if (value instanceof Map) {
            return new MapConfigSection((Map<String, Object>) value);
        }
        return null;
    }

    /**
     * Checks if the config contains a value at the specified path.
     * 
     * @param path the path to check
     * @return true if found, false otherwise
     */
    @Override
    public boolean contains(String path) {
        return get(path) != null;
    }

    /**
     * Gets a set of keys at the root of the config.
     * 
     * @param deep if true, includes all keys recursively
     * @return a Set of keys
     */
    @Override
    public Set<String> getKeys(boolean deep) {
        return getKeysFromMap(data, "", deep);
    }

    /**
     * Recursive helper to retrieve keys from a map.
     * 
     * @param map    the map to inspect
     * @param prefix the key prefix for nested values
     * @param deep   whether to recurse into nested maps
     * @return set of keys
     */
    protected Set<String> getKeysFromMap(Map<String, Object> map, String prefix, boolean deep) {
        Set<String> keys = new LinkedHashSet<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            keys.add(key);

            if (deep && entry.getValue() instanceof Map) {
                keys.addAll(getKeysFromMap((Map<String, Object>) entry.getValue(), key, true));
            }
        }

        return keys;
    }

    /**
     * Gets all values in the config as a Map.
     * 
     * @return a Map containing all config data
     */
    @Override
    public Map<String, Object> getValues() {
        return new LinkedHashMap<>(data);
    }

    /**
     * Gets the file associated with this config.
     * 
     * @return the config File
     */
    @Override
    public File getFile() {
        return file;
    }

    /**
     * Sets the default values for the configuration.
     * 
     * @param defaults a Map of default values
     */
    @Override
    public void setDefaults(Map<String, Object> defaults) {
        this.defaults = new LinkedHashMap<>(defaults);
    }

    /**
     * Adds a single default value.
     * 
     * @param path  the path for the default
     * @param value the default value
     */
    @Override
    public void addDefault(String path, Object value) {
        if (defaults == null) {
            defaults = new LinkedHashMap<>();
        }

        String[] parts = path.split("\\.");
        Map<String, Object> current = defaults;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                current.put(parts[i], newMap);
                current = newMap;
            } else {
                current = (Map<String, Object>) next;
            }
        }

        current.put(parts[parts.length - 1], value);
    }

    /**
     * Applies default values to the current data map.
     * <p>
     * Only adds keys/values that are missing from the current configuration data.
     * </p>
     */
    protected void applyDefaults() {
        if (defaults == null || defaults.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (!data.containsKey(entry.getKey())) {
                data.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Inner class for config sections backed by a Map.
     * <p>
     * Provides a {@link ConfigSection} view over a standard Map, allowing
     * identical access patterns for nested data.
     * </p>
     */
    protected static class MapConfigSection implements ConfigSection {
        private final Map<String, Object> data;

        /**
         * Creates a new config section.
         * 
         * @param data the map backing this section
         */
        public MapConfigSection(Map<String, Object> data) {
            this.data = data;
        }

        /**
         * Gets a value from this section.
         * 
         * @param path the path relative to this section
         * @return the value, or null
         */
        @Override
        public Object get(String path) {
            return get(path, null);
        }

        /**
         * Gets a value from this section with a default.
         * 
         * @param path the path relative to this section
         * @param def  the default value
         * @return the value, or default
         */
        @Override
        public Object get(String path, Object def) {
            if (path == null || path.isEmpty()) {
                return def;
            }

            String[] parts = path.split("\\.");
            Map<String, Object> current = data;

            for (int i = 0; i < parts.length - 1; i++) {
                Object next = current.get(parts[i]);
                if (!(next instanceof Map)) {
                    return def;
                }
                current = (Map<String, Object>) next;
            }

            Object value = current.get(parts[parts.length - 1]);
            return value != null ? value : def;
        }

        /**
         * Gets a String from this section.
         * 
         * @param path the path relative to this section
         * @return the string value
         */
        @Override
        public String getString(String path) {
            return getString(path, null);
        }

        /**
         * Gets a String from this section with a default.
         * 
         * @param path the path relative to this section
         * @param def  the default string
         * @return the string value, or default
         */
        @Override
        public String getString(String path, String def) {
            Object value = get(path, def);
            return value != null ? String.valueOf(value) : def;
        }

        /**
         * Gets an int from this section.
         * 
         * @param path the path relative to this section
         * @return the int value
         */
        @Override
        public int getInt(String path) {
            return getInt(path, 0);
        }

        /**
         * Gets an int from this section with a default.
         * 
         * @param path the path relative to this section
         * @param def  the default int
         * @return the int value, or default
         */
        @Override
        public int getInt(String path, int def) {
            Object value = get(path, def);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            try {
                return Integer.parseInt(String.valueOf(value));
            } catch (NumberFormatException e) {
                return def;
            }
        }

        /**
         * Gets a double from this section.
         * 
         * @param path the path relative to this section
         * @return the double value
         */
        @Override
        public double getDouble(String path) {
            return getDouble(path, 0.0);
        }

        /**
         * Gets a double from this section with a default.
         * 
         * @param path the path relative to this section
         * @param def  the default double
         * @return the double value, or default
         */
        @Override
        public double getDouble(String path, double def) {
            Object value = get(path, def);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            try {
                return Double.parseDouble(String.valueOf(value));
            } catch (NumberFormatException e) {
                return def;
            }
        }

        /**
         * Gets a boolean from this section.
         * 
         * @param path the path relative to this section
         * @return the boolean value
         */
        @Override
        public boolean getBoolean(String path) {
            return getBoolean(path, false);
        }

        /**
         * Gets a boolean from this section with a default.
         * 
         * @param path the path relative to this section
         * @param def  the default boolean
         * @return the boolean value, or default
         */
        @Override
        public boolean getBoolean(String path, boolean def) {
            Object value = get(path, def);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            }
            return def;
        }

        /**
         * Gets a nested section from this section.
         * 
         * @param path the path to the nested section
         * @return the nested ConfigSection, or null
         */
        @Override
        public ConfigSection getSection(String path) {
            Object value = get(path);
            if (value instanceof Map) {
                return new MapConfigSection((Map<String, Object>) value);
            }
            return null;
        }

        /**
         * Gets keys from this section.
         * 
         * @param deep if true, includes recursive keys
         * @return a Set of keys
         */
        @Override
        public Set<String> getKeys(boolean deep) {
            Set<String> keys = new LinkedHashSet<>();
            for (String key : data.keySet()) {
                keys.add(key);
                if (deep && data.get(key) instanceof Map) {
                    MapConfigSection section = new MapConfigSection((Map<String, Object>) data.get(key));
                    for (String subKey : section.getKeys(true)) {
                        keys.add(key + "." + subKey);
                    }
                }
            }
            return keys;
        }

        /**
         * Gets the values of this section as a Map.
         * 
         * @return a Map of the section's data
         */
        @Override
        public Map<String, Object> getValues() {
            return new LinkedHashMap<>(data);
        }

        /**
         * Checks if a value exists in this section.
         * 
         * @param path the path to check
         * @return true if found
         */
        @Override
        public boolean contains(String path) {
            return get(path) != null;
        }
    }
}
