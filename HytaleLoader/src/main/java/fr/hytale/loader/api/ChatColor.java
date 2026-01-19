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
 * @version 1.0.6
 * @since 1.0.6
 */
public class ChatColor {

    // Color constants
    /** Black color code (&amp;0) */
    public static final Color BLACK = new Color(0, 0, 0);
    /** Dark Blue color code (&amp;1) */
    public static final Color DARK_BLUE = new Color(0, 0, 170);
    /** Dark Green color code (&amp;2) */
    public static final Color DARK_GREEN = new Color(0, 170, 0);
    /** Dark Aqua color code (&amp;3) */
    public static final Color DARK_AQUA = new Color(0, 170, 170);
    /** Dark Red color code (&amp;4) */
    public static final Color DARK_RED = new Color(170, 0, 0);
    /** Dark Purple color code (&amp;5) */
    public static final Color DARK_PURPLE = new Color(170, 0, 170);
    /** Gold color code (&amp;6) */
    public static final Color GOLD = new Color(255, 170, 0);
    /** Gray color code (&amp;7) */
    public static final Color GRAY = new Color(170, 170, 170);
    /** Dark Gray color code (&amp;8) */
    public static final Color DARK_GRAY = new Color(85, 85, 85);
    /** Blue color code (&amp;9) */
    public static final Color BLUE = new Color(85, 85, 255);
    /** Green color code (&amp;a) */
    public static final Color GREEN = new Color(85, 255, 85);
    /** Aqua color code (&amp;b) */
    public static final Color AQUA = new Color(85, 255, 255);
    /** Red color code (&amp;c) */
    public static final Color RED = new Color(255, 85, 85);
    /** Light Purple color code (&amp;d) */
    public static final Color LIGHT_PURPLE = new Color(255, 85, 255);
    /** Yellow color code (&amp;e) */
    public static final Color YELLOW = new Color(255, 255, 85);
    /** White color code (&amp;f) */
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

    // Prevent instantiation
    private ChatColor() {
    }

    /**
     * Parses a string with Minecraft-style color codes and returns a Hytale
     * {@link Message}.
     * <p>
     * Supports both '&amp;' and '§' as color code prefixes.
     * It handles colors, bold (&amp;l), and italic (&amp;o).
     * </p>
     * 
     * @param text the text with color codes (e.g., "&amp;aGreen &amp;cRed")
     * @return a formatted {@link Message} object ready to be sent to a player
     */
    public static Message colorize(String text) {
        if (text == null || text.isEmpty()) {
            return new Message.Text("");
        }

        // Translate alternate color codes to section symbol just in case, but we handle
        // both
        String processedText = translateAlternateColorCodes('&', text);

        Message.Builder messageBuilder = Message.builder();

        String[] parts = processedText.split("(?=§[0-9a-fA-FlLoOrR])");

        Color currentColor = null; // Default color
        boolean bold = false;
        boolean italic = false;

        for (String part : parts) {
            if (part.isEmpty())
                continue;

            String content = part;

            // Check if this part starts with a color code
            if (part.startsWith("§")) {
                if (part.length() >= 2) {
                    char code = Character.toLowerCase(part.charAt(1));

                    if (COLOR_MAP.containsKey(code)) {
                        currentColor = COLOR_MAP.get(code);
                        // Reset formats when color changes (Minecraft behavior)
                        bold = false;
                        italic = false;
                    } else if (code == 'l') {
                        bold = true;
                    } else if (code == 'o') {
                        italic = true;
                    } else if (code == 'r') {
                        currentColor = null; // Reset
                        bold = false;
                        italic = false;
                    }

                    // Remove the code from the content
                    if (part.length() > 2) {
                        content = part.substring(2);
                    } else {
                        content = ""; // Just a code, no text
                    }
                }
            }

            if (!content.isEmpty()) {
                Message.Text textComponent = new Message.Text(content);
                if (currentColor != null) {
                    textComponent.color(currentColor);
                }
                if (bold) {
                    textComponent.bold(true);
                }
                if (italic) {
                    textComponent.italic(true);
                }
                messageBuilder.add(textComponent);
            }
        }

        return messageBuilder.build();
    }

    /**
     * Translates '&amp;' color codes to '§' in a string.
     * 
     * @param altColorChar the alternate color character to replace (usually
     *                     '&amp;')
     * @param text         the text with '&amp;' codes
     * @return the text with '§' codes
     */
    public static String translateAlternateColorCodes(char altColorChar, String text) {
        char[] b = text.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = '§';
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
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
}
