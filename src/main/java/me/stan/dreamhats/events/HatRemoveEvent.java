package me.stan.dreamhats.events;

import me.stan.dreamhats.model.Hat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HatRemoveEvent extends Event {

  private static final HandlerList HANDLERS = new HandlerList();
  private final Player player;
  private final Hat hat;

  public HatRemoveEvent(final Player player, final Hat hat) {
    this.player = player;
    this.hat = hat;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
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
}
