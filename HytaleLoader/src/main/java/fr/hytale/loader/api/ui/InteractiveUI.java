package fr.hytale.loader.api.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Simplified interactive UI wrapper that handles codec complexity
 * automatically.
 * <p>
 * This class provides a simple API for creating interactive UIs with buttons,
 * without needing to understand Hytale's codec system.
 * </p>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>
 * InteractiveUI ui = new InteractiveUI("Pages/MyMenu.ui");
 * 
 * ui.onButtonClick("saveButton", (player, data) -> {
 *     player.sendMessage("Save button clicked!");
 * });
 * 
 * player.openInteractiveUI(ui);
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.5
 */
public class InteractiveUI {

    private final String uiFilePath;
    private final UILifetime lifetime;
    private final Map<String, BiConsumer<fr.hytale.loader.api.Player, SimpleEventData>> eventHandlers = new HashMap<>();

    /**
     * Simple event data structure.
     */
    public static class SimpleEventData {
        public String action;

        /**
         * Codec for the simple event data.
         */
        public static final BuilderCodec<SimpleEventData> CODEC = BuilderCodec
                .builder(SimpleEventData.class, SimpleEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (SimpleEventData o, String v) -> o.action = v,
                        (SimpleEventData o) -> o.action)
                .add()
                .build();
    }

    /**
     * Creates a new interactive UI.
     * 
     * @param uiFilePath Path to the UI file (e.g., "Pages/MyUI.ui")
     * @param lifetime   UI lifetime behavior
     */
    public InteractiveUI(@Nonnull String uiFilePath, @Nonnull UILifetime lifetime) {
        this.uiFilePath = uiFilePath;
        this.lifetime = lifetime;
    }

    /**
     * Creates a new interactive UI with CAN_DISMISS lifetime.
     * 
     * @param uiFilePath Path to the UI file
     */
    public InteractiveUI(@Nonnull String uiFilePath) {
        this(uiFilePath, UILifetime.CAN_DISMISS);
    }

    /**
     * Registers a button click handler.
     * 
     * @param buttonId The button ID from the UI file (e.g., "saveButton" for
     *                 "#SaveButton")
     * @param handler  Consumer that receives (Player, eventData)
     * @return This InteractiveUI for chaining
     */
    public InteractiveUI onButtonClick(@Nonnull String buttonId,
            @Nonnull BiConsumer<fr.hytale.loader.api.Player, SimpleEventData> handler) {
        eventHandlers.put(buttonId, handler);
        return this;
    }

    /**
     * Gets the UI file path.
     * 
     * @return The UI file path
     */
    public String getUiFilePath() {
        return uiFilePath;
    }

    /**
     * Gets the UI lifetime.
     * 
     * @return The UI lifetime
     */
    public UILifetime getLifetime() {
        return lifetime;
    }

    /**
     * Creates the native interactive UI page.
     * 
     * @param playerRef The player reference
     * @param player    The player wrapper
     * @return The native interactive UI page
     */
    public InteractiveCustomUIPage<SimpleEventData> createNativePage(@Nonnull PlayerRef playerRef,
            @Nonnull fr.hytale.loader.api.Player player) {
        return new InteractiveCustomUIPage<SimpleEventData>(playerRef, convertLifetime(lifetime),
                SimpleEventData.CODEC) {

            @Override
            public void build(
                    @Nonnull Ref<EntityStore> ref,
                    @Nonnull UICommandBuilder commandBuilder,
                    @Nonnull UIEventBuilder eventBuilder,
                    @Nonnull Store<EntityStore> store) {
                // Load the UI file
                commandBuilder.append(uiFilePath);

                // Register event bindings for each button
                for (Map.Entry<String, BiConsumer<fr.hytale.loader.api.Player, SimpleEventData>> entry : eventHandlers
                        .entrySet()) {
                    String buttonId = entry.getKey();

                    // Add event binding for button click
                    // Format: "#ButtonId" in the UI file
                    // We use the ID exactly as provided, just adding # if missing
                    String selector = buttonId.startsWith("#") ? buttonId : "#" + buttonId;

                    eventBuilder.addEventBinding(
                            CustomUIEventBindingType.Activating,
                            selector,
                            new EventData().append("Action", buttonId));
                }
            }

            @Override
            public void handleDataEvent(
                    @Nonnull Ref<EntityStore> ref,
                    @Nonnull Store<EntityStore> store,
                    @Nonnull SimpleEventData data) {
                try {
                    // Find and execute the handler for this action
                    BiConsumer<fr.hytale.loader.api.Player, SimpleEventData> handler = eventHandlers.get(data.action);
                    if (handler != null) {
                        handler.accept(player, data);
                    } else {
                        System.out.println("[InteractiveUI] No handler for action: " + data.action);
                    }

                    // CRITICAL: Send an empty update to tell the client the action is processed
                    // This stops the "Loading..." spinner on the button
                    sendUpdate(null, false);

                } catch (Exception e) {
                    System.err.println("[InteractiveUI] Error handling event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Capitalizes the first letter of a string (for button IDs).
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Converts custom lifetime to native lifetime.
     */
    private CustomPageLifetime convertLifetime(UILifetime lifetime) {
        return switch (lifetime) {
            case CAN_DISMISS -> CustomPageLifetime.CanDismiss;
            case CANNOT_CLOSE -> CustomPageLifetime.CantClose;
            case CAN_DISMISS_OR_CLOSE_THROUGH_INTERACTION -> CustomPageLifetime.CanDismissOrCloseThroughInteraction;
        };
    }

    /**
     * UI lifetime options.
     */
    public enum UILifetime {
        /**
         * Player can dismiss the UI (e.g., pressing escape).
         */
        CAN_DISMISS,

        /**
         * Player cannot close the UI.
         */
        CANNOT_CLOSE,

        /**
         * UI can be dismissed or closed through interaction.
         */
        CAN_DISMISS_OR_CLOSE_THROUGH_INTERACTION
    }
}
