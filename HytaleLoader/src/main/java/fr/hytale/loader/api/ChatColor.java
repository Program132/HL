package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.Message;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for working with colored text and messages in Hytale.
 * <p>
 * Provides Minecraft-style color codes and methods to parse colored text into
 * Hytale Messages via JSON.
 * </p>
 *
 * <p>
 * <b>Usage Examples:</b>
 * </p>
 *
 * <pre>
 * // Using utility method (Recommended)
 * Message msg1 = ChatColor.colorize("&amp;aHello &amp;cWorld");
 *
 * // Using constants
 * String msg2 = ChatColor.RED + "Error!";
 * </pre>
 *
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.6
 */
public class ChatColor {

    // Color constants
    /** Black color code (&amp;0) */
    public static final String BLACK = "&0";
    /** Dark Blue color code (&amp;1) */
    public static final String DARK_BLUE = "&1";
    /** Dark Green color code (&amp;2) */
    public static final String DARK_GREEN = "&2";
    /** Dark Aqua color code (&amp;3) */
    public static final String DARK_AQUA = "&3";
    /** Dark Red color code (&amp;4) */
    public static final String DARK_RED = "&4";
    /** Dark Purple color code (&amp;5) */
    public static final String DARK_PURPLE = "&5";
    /** Gold color code (&amp;6) */
    public static final String GOLD = "&6";
    /** Gray color code (&amp;7) */
    public static final String GRAY = "&7";
    /** Dark Gray color code (&amp;8) */
    public static final String DARK_GRAY = "&8";
    /** Blue color code (&amp;9) */
    public static final String BLUE = "&9";
    /** Green color code (&amp;a) */
    public static final String GREEN = "&a";
    /** Aqua color code (&amp;b) */
    public static final String AQUA = "&b";
    /** Red color code (&amp;c) */
    public static final String RED = "&c";
    /** Light Purple color code (&amp;d) */
    public static final String LIGHT_PURPLE = "&d";
    /** Yellow color code (&amp;e) */
    public static final String YELLOW = "&e";
    /** White color code (&amp;f) */
    public static final String WHITE = "&f";

    // Formatting
    /** Obfuscated format code (&amp;k) */
    public static final String OBFUSCATED = "&k";
    /** Bold format code (&amp;l) */
    public static final String BOLD = "&l";
    /** Strikethrough format code (&amp;m) */
    public static final String STRIKETHROUGH = "&m";
    /** Underline format code (&amp;n) */
    public static final String UNDERLINE = "&n";
    /** Italic format code (&amp;o) */
    public static final String ITALIC = "&o";
    /** Reset format code (&amp;r) */
    public static final String RESET = "&r";

    private static final Map<Character, String> COLOR_NAMES = new HashMap<>();

    static {
        COLOR_NAMES.put('0', "black");
        COLOR_NAMES.put('1', "dark_blue");
        COLOR_NAMES.put('2', "dark_green");
        COLOR_NAMES.put('3', "dark_aqua");
        COLOR_NAMES.put('4', "dark_red");
        COLOR_NAMES.put('5', "dark_purple");
        COLOR_NAMES.put('6', "gold");
        COLOR_NAMES.put('7', "gray");
        COLOR_NAMES.put('8', "dark_gray");
        COLOR_NAMES.put('9', "blue");
        COLOR_NAMES.put('a', "green");
        COLOR_NAMES.put('b', "aqua");
        COLOR_NAMES.put('c', "red");
        COLOR_NAMES.put('d', "light_purple");
        COLOR_NAMES.put('e', "yellow");
        COLOR_NAMES.put('f', "white");
    }

    // Prevent instantiation
    private ChatColor() {
        throw new UnsupportedOperationException("ChatColor is a utility class");
    }

    /**
     * Parses a string with Minecraft-style color codes and returns a Hytale
     * {@link Message}.
     * <p>
     * Supports both '&amp;' and '&sect;' as color code prefixes.
     * It handles colors, bold (&amp;l), italic (&amp;o), and underline (&amp;n).
     * </p>
     * 
     * @param text the text with color codes (e.g., "&amp;aGreen &amp;cRed")
     * @return a formatted {@link Message} object ready to be sent to a player
     */
    public static Message colorize(String text) {
        if (text == null || text.isEmpty()) {
            return Message.raw("");
        }

        // Convert to section symbol for consistent parsing
        String processed = text.replace('&', '§');

        // Simple parser to JSON
        StringBuilder json = new StringBuilder("{\"text\":\"\",\"extra\":[");

        String[] parts = processed.split("(?=§)");

        String color = null;
        boolean bold = false;
        boolean italic = false;
        boolean underlined = false;

        for (String part : parts) {
            String content = part;
            if (part.startsWith("§") && part.length() >= 2) {
                char code = Character.toLowerCase(part.charAt(1));

                if (COLOR_NAMES.containsKey(code)) {
                    color = COLOR_NAMES.get(code);
                    bold = false;
                    italic = false;
                    underlined = false;
                } else if (code == 'l') {
                    bold = true;
                } else if (code == 'o') {
                    italic = true;
                } else if (code == 'n') {
                    underlined = true;
                } else if (code == 'r') {
                    color = null;
                    bold = false;
                    italic = false;
                    underlined = false;
                }

                if (part.length() > 2) {
                    content = part.substring(2);
                } else {
                    content = "";
                }
            }

            if (!content.isEmpty()) {
                json.append("{");
                json.append("\"text\":\"").append(escapeJson(content)).append("\"");

                if (color != null) {
                    json.append(",\"color\":\"").append(color).append("\"");
                }
                if (bold) {
                    json.append(",\"bold\":true");
                }
                if (italic) {
                    json.append(",\"italic\":true");
                }
                if (underlined) {
                    json.append(",\"underlined\":true");
                }

                json.append("},");
            }
        }

        // Remove trailing comma if exists
        if (json.length() > 0 && json.charAt(json.length() - 1) == ',') {
            json.deleteCharAt(json.length() - 1);
        }

        json.append("]}");

        try {
            return Message.parse(json.toString());
        } catch (Exception e) {
            // Fallback to raw text if parsing fails
            return Message.raw(processed);
        }
    }

    private static String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}