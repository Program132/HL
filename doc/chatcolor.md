# ChatColor API Documentation

The `ChatColor` utility helps you format text using standard Minecraft-style color codes (`&` or `§`). It integrates directly with Hytale's native `Message` system to support colors, bold, italics, etc.

## Usage

### 1. Sending Colored Messages to Players
The easiest way to send a formatted message is using the helper method in the `Player` class.

```java
// Basic colors
player.sendColoredMessage("&cHello &6World!");

// complex formatting (Red, Bold, Italic)
player.sendColoredMessage("&c&lCRITICAL ERROR &7&o(check console)");
```

### 2. Manual String Colorization
If you need to format text for other uses (like Item names, Scoreboards, or broadcast messages), use the static utility.

```java
// Returns a native Hytale Message object
Message msg = ChatColor.colorize("&aSuccess!");

// Using constants directly
String prefix = ChatColor.BLUE + "[Server] ";
```

## Color Codes Reference

| Code | Color        | Code | Color        |
|------|--------------|------|--------------|
| `&0` | Black        | `&8` | Dark Gray    |
| `&1` | Dark Blue    | `&9` | Blue         |
| `&2` | Dark Green   | `&a` | Green        |
| `&3` | Dark Aqua    | `&b` | Aqua         |
| `&4` | Dark Red     | `&c` | Red          |
| `&5` | Dark Purple  | `&d` | Light Purple |
| `&6` | Gold         | `&e` | Yellow       |
| `&7` | Gray         | `&f` | White        |

## Formatting Codes

| Code | Style         |
|------|---------------|
| `&l` | **Bold**      |
| `&o` | *Italic*      |
| `&n` | <u>Underline</u> |
| `&m` | Strikethrough |
| `&r` | Reset         |
