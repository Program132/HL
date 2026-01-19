package fr.hytale.loader.api.ui;

import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.entities.player.pages.BasicCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

/**
 * Simple wrapper for creating custom UIs in Hytale.
 * <p>
 * Custom UIs require a `.ui` file in `resources/common/UI/custom/pages/`.
 * </p>
 * 
 * <h2>Example Usage:</h2>
 * 
 * <pre>
 * // Create UI file at: resources/common/UI/custom/pages/MyCustomUI.ui
 * // Then create a CustomUI instance:
 * 
 * CustomUI ui = new CustomUI("common/UI/custom/pages/MyCustomUI.ui", CustomUILifetime.CAN_DISMISS);
 * player.openCustomUI(ui);
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.5
 */
public class CustomUI {

    private final String uiFilePath;
    private final CustomUILifetime lifetime;

    /**
     * Creates a new custom UI.
     * 
     * @param uiFilePath Path to the UI file (e.g.,
     *                   "common/UI/custom/pages/MyUI.ui")
     * @param lifetime   UI lifetime behavior
     */
    public CustomUI(@Nonnull String uiFilePath, @Nonnull CustomUILifetime lifetime) {
        this.uiFilePath = uiFilePath;
        this.lifetime = lifetime;
    }

    /**
     * Creates a new custom UI with CAN_DISMISS lifetime.
     * 
     * @param uiFilePath Path to the UI file
     */
    public CustomUI(@Nonnull String uiFilePath) {
        this(uiFilePath, CustomUILifetime.CAN_DISMISS);
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
    public CustomUILifetime getLifetime() {
        return lifetime;
    }

    /**
     * Creates the native UI page for this custom UI.
     * 
     * @param playerRef The player reference
     * @return The native UI page
     */
    public BasicCustomUIPage createNativePage(@Nonnull PlayerRef playerRef) {
        return new BasicCustomUIPage(playerRef, convertLifetime(lifetime)) {
            @Override
            public void build(UICommandBuilder builder) {
                builder.append(uiFilePath);
            }
        };
    }

    /**
     * Converts custom lifetime to native lifetime.
     */
    private CustomPageLifetime convertLifetime(CustomUILifetime lifetime) {
        return switch (lifetime) {
            case CAN_DISMISS -> CustomPageLifetime.CanDismiss;
            case CANNOT_CLOSE -> CustomPageLifetime.CantClose;
            case CAN_DISMISS_OR_CLOSE_THROUGH_INTERACTION -> CustomPageLifetime.CanDismissOrCloseThroughInteraction;
        };
    }

    /**
     * UI lifetime options.
     */
    public enum CustomUILifetime {
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
