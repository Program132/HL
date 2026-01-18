package fr.hytale.loader.config;

import java.io.*;
import java.util.*;

/**
 * YAML-based configuration implementation.
 * <p>
 * This class handles parsing and generating YAML files for configuration
 * storage.
 * It extends {@link BaseConfig} to leverage the common configuration logic.
 * </p>
 * <p>
 * The YAML parser is simple and supports standard YAML constructs like
 * key-value pairs, nested maps, and lists.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 * @see BaseConfig
 */
public class YamlConfig extends BaseConfig {

    /**
     * Creates a new YAML configuration linked to the specified file.
     * 
     * @param file the configuration file (usually ending in .yml)
     */
    public YamlConfig(File file) {
        super(file);
    }

    /**
     * Saves the current configuration data to the file in YAML format.
     * <p>
     * Creates parent directories if they do not exist.
     * </p>
     * 
     * @throws IOException if an error occurs while writing to the file
     */
    @Override
    public void save() throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writeYaml(writer, data, 0);
        }
    }

    /**
     * Recursively writes data to the writer in YAML format.
     * 
     * @param writer the writer to write to
     * @param map    the map data to write
     * @param indent the current indentation level
     * @throws IOException if writing fails
     */
    private void writeYaml(BufferedWriter writer, Map<String, Object> map, int indent) throws IOException {
        String indentStr = "  ".repeat(indent);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                writer.write(indentStr + key + ":\n");
                writeYaml(writer, (Map<String, Object>) value, indent + 1);
            } else if (value instanceof List) {
                writer.write(indentStr + key + ":\n");
                for (Object item : (List<?>) value) {
                    writer.write(indentStr + "  - " + formatValue(item) + "\n");
                }
            } else {
                writer.write(indentStr + key + ": " + formatValue(value) + "\n");
            }
        }
    }

    /**
     * Formats a value for YAML output, escaping strings if necessary.
     * 
     * @param value the value to format
     * @return the string representation suitable for YAML
     */
    private String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            String str = (String) value;
            if (str.contains(":") || str.contains("#") || str.contains("'") || str.contains("\"")) {
                return "'" + str.replace("'", "''") + "'";
            }
            return str;
        }
        return String.valueOf(value);
    }

    /**
     * Reloads the configuration from the file.
     * <p>
     * This clears the current data and re-parses the file.
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
            // Create file with defaults
            if (!defaults.isEmpty()) {
                data.putAll(defaults);
                save();
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            parseYaml(reader, data);
        }

        // Apply defaults for missing keys
        applyDefaults();
    }

    /**
     * Parses YAML content from the reader into the root map.
     * 
     * @param reader the reader to read from
     * @param root   the map to populate
     * @throws IOException if reading fails
     */
    private void parseYaml(BufferedReader reader, Map<String, Object> root) throws IOException {
        Stack<Map<String, Object>> stack = new Stack<>();
        Stack<Integer> indents = new Stack<>();
        stack.push(root);
        indents.push(-1);

        String line;

        while ((line = reader.readLine()) != null) {
            // Skip empty lines and comments
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }

            int indent = getIndent(line);
            String content = line.trim();

            // Handle indentation levels
            while (!indents.isEmpty() && indent <= indents.peek()) {
                indents.pop();
                stack.pop();
            }

            if (content.startsWith("- ")) {
                // List item
                String value = content.substring(2).trim();
                Map<String, Object> current = stack.peek();

                // Find or create list for last key
                if (!current.isEmpty()) {
                    List<Object> values = new ArrayList<>(current.values());
                    if (values.isEmpty())
                        continue;

                    Object lastValue = values.get(values.size() - 1);
                    if (!(lastValue instanceof List)) {
                        List<Object> list = new ArrayList<>();
                        list.add(parseValue(value));

                        List<String> keys = new ArrayList<>(current.keySet());
                        String lastKey = keys.get(keys.size() - 1);
                        current.put(lastKey, list);
                    } else {
                        ((List<Object>) lastValue).add(parseValue(value));
                    }
                }
            } else if (content.contains(":")) {
                // Key-value pair
                int colonIndex = content.indexOf(':');
                String key = content.substring(0, colonIndex).trim();
                String valueStr = content.substring(colonIndex + 1).trim();

                Map<String, Object> current = stack.peek();

                if (valueStr.isEmpty()) {
                    // Nested map
                    Map<String, Object> nested = new LinkedHashMap<>();
                    current.put(key, nested);
                    stack.push(nested);
                    indents.push(indent);
                } else {
                    // Simple value
                    current.put(key, parseValue(valueStr));
                }
            }
        }
    }

    /**
     * Calculates the indentation level of a line.
     * 
     * @param line the line to check
     * @return the number of spaces of indentation (tabs count as 2 spaces)
     */
    private int getIndent(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                count++;
            } else if (c == '\t') {
                count += 2;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * Parses a string value into its appropriate type (Boolean, Number, String).
     * 
     * @param value the string value
     * @return the parsed object
     */
    private Object parseValue(String value) {
        if (value == null || value.equals("null")) {
            return null;
        }

        // Remove quotes
        if ((value.startsWith("'") && value.endsWith("'")) ||
                (value.startsWith("\"") && value.endsWith("\""))) {
            return value.substring(1, value.length() - 1).replace("''", "'");
        }

        // Boolean
        if (value.equalsIgnoreCase("true")) {
            return true;
        }
        if (value.equalsIgnoreCase("false")) {
            return false;
        }

        // Number
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            // Not a number, return as string
        }

        return value;
    }
}
