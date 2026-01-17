package fr.hytale.loader.config;

import java.io.*;
import java.util.*;

/**
 * YAML-based configuration implementation.
 * <p>
 * Provides a simple YAML parser and writer for plugin configurations.
 * Supports nested maps, lists, and basic data types.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.4
 * @since 1.0.4
 */
public class YamlConfig implements Config {

    private final File file;
    private final Map<String, Object> data;
    private Map<String, Object> defaults;

    /**
     * Creates a new YAML config.
     * 
     * @param file the config file
     */
    public YamlConfig(File file) {
        this.file = file;
        this.data = new LinkedHashMap<>();
        this.defaults = new LinkedHashMap<>();
    }

    @Override
    public Object get(String path) {
        return get(path, null);
    }

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
                // Check defaults
                return getFromDefaults(path, def);
            }
            current = (Map<String, Object>) next;
        }

        Object value = current.get(parts[parts.length - 1]);
        return value != null ? value : getFromDefaults(path, def);
    }

    private Object getFromDefaults(String path, Object def) {
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

    @Override
    public String getString(String path) {
        return getString(path, null);
    }

    @Override
    public String getString(String path, String def) {
        Object value = get(path, def);
        return value != null ? String.valueOf(value) : def;
    }

    @Override
    public int getInt(String path) {
        return getInt(path, 0);
    }

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

    @Override
    public double getDouble(String path) {
        return getDouble(path, 0.0);
    }

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

    @Override
    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

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

    @Override
    public List<?> getList(String path) {
        return getList(path, null);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        Object value = get(path, def);
        return value instanceof List ? (List<?>) value : def;
    }

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

    @Override
    public ConfigSection getSection(String path) {
        Object value = get(path);
        if (value instanceof Map) {
            return new YamlConfigSection((Map<String, Object>) value);
        }
        return null;
    }

    @Override
    public boolean contains(String path) {
        return get(path) != null;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return getKeysFromMap(data, "", deep);
    }

    private Set<String> getKeysFromMap(Map<String, Object> map, String prefix, boolean deep) {
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

    @Override
    public Map<String, Object> getValues() {
        return new LinkedHashMap<>(data);
    }

    @Override
    public void save() throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writeYaml(writer, data, 0);
        }
    }

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

    private void applyDefaults() {
        if (defaults == null || defaults.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (!data.containsKey(entry.getKey())) {
                data.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void parseYaml(BufferedReader reader, Map<String, Object> root) throws IOException {
        Stack<Map<String, Object>> stack = new Stack<>();
        Stack<Integer> indents = new Stack<>();
        stack.push(root);
        indents.push(-1);

        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;

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
                    Object lastValue = new ArrayList<>(current.values()).get(current.size() - 1);
                    if (!(lastValue instanceof List)) {
                        List<Object> list = new ArrayList<>();
                        list.add(parseValue(value));
                        String lastKey = new ArrayList<>(current.keySet()).get(current.size() - 1);
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

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setDefaults(Map<String, Object> defaults) {
        this.defaults = new LinkedHashMap<>(defaults);
    }

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
     * Inner class for config sections.
     */
    private static class YamlConfigSection implements ConfigSection {
        private final Map<String, Object> data;

        public YamlConfigSection(Map<String, Object> data) {
            this.data = data;
        }

        @Override
        public Object get(String path) {
            return get(path, null);
        }

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

        @Override
        public String getString(String path) {
            return getString(path, null);
        }

        @Override
        public String getString(String path, String def) {
            Object value = get(path, def);
            return value != null ? String.valueOf(value) : def;
        }

        @Override
        public int getInt(String path) {
            return getInt(path, 0);
        }

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

        @Override
        public double getDouble(String path) {
            return getDouble(path, 0.0);
        }

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

        @Override
        public boolean getBoolean(String path) {
            return getBoolean(path, false);
        }

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

        @Override
        public ConfigSection getSection(String path) {
            Object value = get(path);
            if (value instanceof Map) {
                return new YamlConfigSection((Map<String, Object>) value);
            }
            return null;
        }

        @Override
        public Set<String> getKeys(boolean deep) {
            Set<String> keys = new LinkedHashSet<>();
            for (String key : data.keySet()) {
                keys.add(key);
                if (deep && data.get(key) instanceof Map) {
                    YamlConfigSection section = new YamlConfigSection((Map<String, Object>) data.get(key));
                    for (String subKey : section.getKeys(true)) {
                        keys.add(key + "." + subKey);
                    }
                }
            }
            return keys;
        }

        @Override
        public Map<String, Object> getValues() {
            return new LinkedHashMap<>(data);
        }

        @Override
        public boolean contains(String path) {
            return get(path) != null;
        }
    }
}
