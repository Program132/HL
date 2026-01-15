package fr.hytale.loader.event;

import com.hypixel.hytale.event.EventPriority;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler.
 * <p>
 * Methods annotated with {@code @EventHandler} will automatically be registered
 * as event listeners when the plugin is enabled. The method must have exactly
 * one
 * parameter which is the event type to listen for.
 * </p>
 * 
 * <h2>Usage Example:</h2>
 * 
 * <pre>
 * {@code
 * &#64;EventHandler
 * public void onPlayerJoin(PlayerJoinEvent event) {
 *     getLogger().info("Player joined: " + event.getPlayerName());
 * }
 * 
 * @EventHandler(priority = EventPriority.HIGH)
 * public void onPlayerChat(PlayerChatEvent event) {
 *     // This handler runs with HIGH priority
 * }
 * }
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.0
 * @see EventPriority
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    /**
     * The priority of this event handler.
     * <p>
     * Higher priority handlers are executed first.
     * Default is {@link EventPriority#NORMAL}.
     * </p>
     * 
     * @return the event priority
     */
    EventPriority priority() default EventPriority.NORMAL;
}
