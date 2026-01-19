# UI API Reference

Complete reference for the HytaleLoader Custom UI system.

## Overview

The UI API allows mods to display custom interfaces to players. Hytale uses a declarative system with `.ui` files.

**IMPORTANT**: Custom `.ui` files MUST be installed on BOTH the server and inside the ressources plugin folder:
- Server path: `server/Assets/Common/UI/Custom/Pages/`
- Ressources path: `ressources/Common/UI/Custom/Pages/`

**Note**: The path of your UI will be only `Pages/<file>.ui` and not `Common/UI/Custom/Pages/`.

## Interactive UI

The `InteractiveUI` class allows you to create UIs with clickable buttons and event handling.

### Creating an Interactive UI

```java
// Create UI wrapper for your file
InteractiveUI ui = new InteractiveUI("Pages/MyMenu.ui");

// Handle button clicks (matches #myButton in .ui file)
ui.onButtonClick("myButton", (player, data) -> {
    player.sendMessage("Button clicked!");
    player.sendMessage("Action: " + data.action);
});

// Handle close button
ui.onButtonClick("closeButton", (player, data) -> {
    player.sendMessage("Closing menu...");
    player.closeCustomUI(); // Create method to close UI
});

// Open the UI for the player
if (player.openInteractiveUI(ui)) {
    // success
}
```

### UI File Example (.ui)

Save this file as `MyMenu.ui` in the `Pages` folder.

```css
@BtnStyle = TextButtonStyle(
  Default: (Background: #4CAF50, LabelStyle: (FontSize: 16, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)),
  Hovered: (Background: #45a049, LabelStyle: (FontSize: 16, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center)),
  Pressed: (Background: #388E3C, LabelStyle: (FontSize: 16, TextColor: #ffffff, RenderBold: true, HorizontalAlignment: Center, VerticalAlignment: Center))
);

Group {
  Anchor: (Width: 400, Height: 300);
  Background: #1a1a1a(0.95);
  LayoutMode: Top;
  Padding: (Full: 20);
  
  Label {
    Text: "My Menu";
    Anchor: (Height: 40);
    Style: (FontSize: 24, TextColor: #ffffff, HorizontalAlignment: Center);
  }
  
  // Button with ID #myButton
  TextButton #myButton {
    Text: "Click Me!";
    Anchor: (Width: 200, Height: 40);
    Style: @BtnStyle;
  }
}
```

## Basic UI (Non-Interactive)

For simple display pages (info, welcome screens) without complex interaction logic.

```java
CustomUI ui = new CustomUI("Pages/Welcome.ui");
player.openCustomUI(ui);
```

## Managing UIs

### Closing UI

You can programmatically close any active custom UI for a player.

```java
player.closeCustomUI();
```

## See Also

- [Player API](player_api.md)
- [World API](world_api.md)
