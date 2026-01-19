package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

/**
 * Called when a player sends a chat message.
 * <p>
 * This event allows mods to intercept, modify, or cancel chat messages.
 * The message can be modified before being broadcast to other players,
 * and the event can be cancelled to prevent the message from being sent.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.0
 */
public class PlayerChatEvent implements IEvent<Void> {

    private final com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent originalEvent;

    public PlayerChatEvent(com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent originalEvent) {
        this.originalEvent = originalEvent;
    }

    public PlayerRef getSender() {
        return originalEvent.getSender();
    }

    public String getMessage() {
        return originalEvent.getContent();
    }

    public void setMessage(String message) {
        originalEvent.setContent(message);
    }

    public void setCancelled(boolean cancelled) {
        originalEvent.setCancelled(cancelled);
    }

    public boolean isCancelled() {
        return originalEvent.isCancelled();
    }
}
