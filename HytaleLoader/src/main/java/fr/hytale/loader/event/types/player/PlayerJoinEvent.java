package fr.hytale.loader.event.types;

import com.hypixel.hytale.event.IEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;

public class PlayerJoinEvent implements IEvent<Void> {

    private final Player player;
    private final PlayerRef playerRef;
    private final AddPlayerToWorldEvent originalEvent;

    public PlayerJoinEvent(Player player, PlayerRef playerRef, AddPlayerToWorldEvent originalEvent) {
        this.player = player;
        this.playerRef = playerRef;
        this.originalEvent = originalEvent;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public String getPlayerName() {
        return playerRef.getUsername();
    }

    public AddPlayerToWorldEvent getOriginalEvent() {
        return originalEvent;
    }

    public void setJoinMessage(boolean broadcast) {
        originalEvent.setBroadcastJoinMessage(broadcast);
    }
}
