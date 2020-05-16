package me.stan.hatmanager.Events;

import me.stan.hatmanager.Model.Hat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HatAddEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Hat hat;

    public HatAddEvent(Player player, Hat hat) {
        this.player = player;
        this.hat = hat;
    }

    public Player getPlayer() {
        return player;
    }

    public Hat getHat() {
        return hat;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
