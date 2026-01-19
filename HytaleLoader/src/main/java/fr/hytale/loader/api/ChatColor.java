package fr.hytale.loader.api;

import com.hypixel.hytale.server.core.Message;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for working with colored text and messages in Hytale.
 * <p>
 * Provides Minecraft-style color codes and methods to parse colored text into
 * Hytale Messages.
 * </p>
 * 
 * <h3>Usage Examples:</h3>
 * 
 * <pre>{@code
 * // Direct color constants
 * player.sendMessage(Message.raw("Error!").color(ChatColor.RED));
 * 
 * // Parse color codes
 * Message msg = ChatColor.colorize("&6Gold &eYellow &cRed");
 * player.sendMessage(msg);
 * 
 * // Using player helper method
 * player.sendColoredMessage("&aGreen text &cRed text");
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public class ChatColor {

    // Minecraft color palette (RGB values)
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color DARK_BLUE = new Color(0, 0, 170);
    public static final Color DARK_GREEN = new Color(0, 170, 0);
    public static final Color DARK_AQUA = new Color(0, 170, 170);
    public static final Color DARK_RED = new Color(170, 0, 0);
    public static final Color DARK_PURPLE = new Color(170, 0, 170);
    public static final Color GOLD = new Color(255, 170, 0);
    public static final Color GRAY = new Color(170, 170, 170);
    public static final Color DARK_GRAY = new Color(85, 85, 85);
    public static final Color BLUE = new Color(85, 85, 255);
    public static final Color GREEN = new Color(85, 255, 85);
    public static final Color AQUA = new Color(85, 255, 255);
    public static final Color RED = new Color(255, 85, 85);
    public static final Color LIGHT_PURPLE = new Color(255, 85, 255);
    public static final Color YELLOW = new Color(255, 255, 85);
    public static final Color WHITE = new Color(255, 255, 255);

    // Color code mapping
    private static final Map<Character, Color> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put('0', BLACK);
        COLOR_MAP.put('1', DARK_BLUE);
        COLOR_MAP.put('2', DARK_GREEN);
        COLOR_MAP.put('3', DARK_AQUA);
        COLOR_MAP.put('4', DARK_RED);
        COLOR_MAP.put('5', DARK_PURPLE);
        COLOR_MAP.put('6', GOLD);
        COLOR_MAP.put('7', GRAY);
        COLOR_MAP.put('8', DARK_GRAY);
        COLOR_MAP.put('9', BLUE);
        COLOR_MAP.put('a', GREEN);
        COLOR_MAP.put('b', AQUA);
        COLOR_MAP.put('c', RED);
        COLOR_MAP.put('d', LIGHT_PURPLE);
        COLOR_MAP.put('e', YELLOW);
        COLOR_MAP.put('f', WHITE);
    }

    /**
     * Parses a string with Minecraft color codes and returns a formatted Message.
     * <p>
     * Supports both '&' and '§' as color code prefixes.
     * Color codes: 0-9, a-f for colors; l=bold, o=italic, r=reset
     * </p>
     * 
     * @param text the text with color codes (e.g., "&aGreen &cRed")
     * @return a formatted Hytale Message with colors applied
     */
    public static Message colorize(String text) {
        if (text == null || text.isEmpty()) {
            return Message.raw("");
        }

        // Convert & to §
        text = text.replace('&', '§');

        List<Message> parts = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        Color currentColor = null;
        boolean bold = false;
        boolean italic = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '§' && i + 1 < text.length()) {
                // Flush current text
                if (currentText.length() > 0) {
                    Message part = Message.raw(currentText.toString());
                    if (currentColor != null) {
                        part = part.color(currentColor);
                    }
                    if (bold) {
                        part = part.bold(true);
                    }
                    if (italic) {
                        part = part.italic(true);
                    }
                    parts.add(part);
                    currentText = new StringBuilder();
                }

                // Parse color code
                char code = Character.toLowerCase(text.charAt(i + 1));

                if (code == 'r') {
                    // Reset
                    currentColor = null;
                    bold = false;
                    italic = false;
                } else if (code == 'l') {
                    bold = true;
                } else if (code == 'o') {
                    italic = true;
                } else if (COLOR_MAP.containsKey(code)) {
                    currentColor = COLOR_MAP.get(code);
                }

                i++; // Skip next character
            } else {
                currentText.append(c);
            }
        }

        // Flush remaining text
        if (currentText.length() > 0) {
            Message part = Message.raw(currentText.toString());
            if (currentColor != null) {
                part = part.color(currentColor);
            }
            if (bold) {
                part = part.bold(true);
            }
            if (italic) {
                part = part.italic(true);
            }
            parts.add(part);
        }

        // Join all parts
        if (parts.isEmpty()) {
            return Message.raw("");
        } else if (parts.size() == 1) {
            return parts.get(0);
        } else {
            return Message.join(parts.toArray(new Message[0]));
        }
    }

    /**
     * Translates '&' color codes to '§' in a string.
     * 
     * @param text the text with '&' codes
     * @return the text with '§' codes
     */
    public static String translateAlternateColorCodes(String text) {
        if (text == null) {
            return null;
        }
        return text.replace('&', '§');
    }

    /**
     * Strips all color codes from a string.
     * 
     * @param text the colored text
     * @return the text without color codes
     */
    public static String stripColor(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("§[0-9a-flonmr]", "");
    }

    /**
     * Gets a Color from a color code character.
     * 
     * @param code the color code (0-9, a-f)
     * @return the Color, or null if not found
     */
    public static Color getColorFromCode(char code) {
        return COLOR_MAP.get(Character.toLowerCase(code));
    }

    // Private constructor to prevent instantiation
    private ChatColor() {
        throw new UnsupportedOperationException("ChatColor is a utility class");
    }
}
