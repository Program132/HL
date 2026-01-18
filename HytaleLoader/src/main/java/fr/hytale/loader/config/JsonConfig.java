package fr.hytale.loader.config;

import com.google.gson.*;
import java.io.*;
import java.util.*;

/**
 * JSON-based configuration implementation.
 * <p>
 * This class handles parsing and generating JSON files for configuration
 * storage.
 * It uses the Google Gson library to handle the JSON protocol.
 * It extends {@link BaseConfig} to leverage the common configuration Logic.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 * @see BaseConfig
 */
public class JsonConfig extends BaseConfig {

    /** Global Gson instance for pretty printing and parsing. */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Creates a new JSON configuration linked to the specified file.
     * 
     * @param file the configuration file (usually ending in .json)
     */
    public JsonConfig(File file) {
        super(file);
    }

    /**
     * Saves the current configuration data to the file in JSON format.
     * <p>
     * Creates parent directories if they do not exist.
     * Uses pretty printing to make the file human-readable.
     * </p>
     * 
     * @throws IOException if an error occurs while writing to the file
     */
    @Override
    public void save() throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (Writer writer = new FileWriter(file)) {
            GSON.toJson(data, writer);
        }
    }

    /**
     * Reloads the configuration from the file.
     * <p>
     * This clears the current data and re-parses the file using Gson.
     * If the file does not exist but defaults are set, it creates the file
     * with the default values.
     * </p>
     * 
     * @throws IOException if an error occurs while reading the file
     */
    @Override
    public void reload() throws IOException {
        data.clear();

        if (!file.exists()) {
            if (!defaults.isEmpty()) {
                data.putAll(defaults);
                save();
            }
            return;
        }

        try (Reader reader = new FileReader(file)) {
            // Use Gson to parse directly to JsonObject and convert manually
            // to ensure we have standard Maps and Lists that our BaseConfig expects
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json != null) {
                data.putAll(toMap(json));
            }
        }

        applyDefaults();
    }

    /**
     * Converts a Gson JsonObject to a standard Java Map.
     * 
     * @param obj the JsonObject to convert
     * @return a Map representation of the JSON object
     */
    private Map<String, Object> toMap(JsonObject obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            map.put(entry.getKey(), toValue(entry.getValue()));
        }
        return map;
    }

    /**
     * Converts a generic JsonElement to a standard Java object (Map, List, or
     * primitive).
     * 
     * @param el the JsonElement to convert
     * @return the Java object representation, or null
     */
    private Object toValue(JsonElement el) {
        if (el == null || el.isJsonNull())
            return null;

        if (el.isJsonObject()) {
            return toMap(el.getAsJsonObject());
        }

        if (el.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonElement e : el.getAsJsonArray()) {
                list.add(toValue(e));
            }
            return list;
        }

        if (el.isJsonPrimitive()) {
            JsonPrimitive p = el.getAsJsonPrimitive();
            if (p.isBoolean())
                return p.getAsBoolean();
            if (p.isString())
                return p.getAsString();
            if (p.isNumber()) {
                Number n = p.getAsNumber();
                // Check if number is an integer (no fractional part)
                double d = n.doubleValue();
                if (d == Math.rint(d)) {
                    return n.intValue();
                }
                return d;
            }
        }

        return null;
    }
}
