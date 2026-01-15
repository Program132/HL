package fr.hytale.loader.event.types.player;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

public class PlayerQuitEvent implements IEvent<Void> {

    private final PlayerDisconnectEvent originalEvent;

    public PlayerQuitEvent(PlayerDisconnectEvent originalEvent) {
        this.originalEvent = originalEvent;
    }

    public Player getPlayer() {
        return originalEvent.getPlayerRef().getComponent(Player.getComponentType());
    }

    public String getPlayerName() {
        return originalEvent.getPlayerRef().getUsername();
    }

    public PlayerDisconnectEvent getOriginalEvent() {
        return originalEvent;
    }
}

