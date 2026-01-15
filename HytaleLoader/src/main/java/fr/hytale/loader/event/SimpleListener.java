package fr.hytale.loader.event;

/**
 * Marker interface for classes that can listen to HytaleLoader events.
 * <p>
 * This interface is used to identify listener classes. Any class implementing
 * this interface can have methods annotated with {@link EventHandler} to
 * receive
 * event notifications.
 * </p>
 * <p>
 * This interface intentionally contains no methods - it serves solely as a
 * marker
 * to indicate that a class is an event listener.
 * </p>
 * 
 * <h2>Usage Example:</h2>
 * 
 * <pre>{@code
 * public class MyListener implements SimpleListener {
 *     @EventHandler
 *     public void onPlayerJoin(PlayerJoinEvent event) {
 *         // Handle player join
 *     }
 * }
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.0
 * @see EventHandler
 */
public interface SimpleListener {
}
