package me.stan.dreamhats.model;

import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.events.HatAddEvent;
import me.stan.dreamhats.events.HatRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HatPlayer {
  private UUID playerUUID;
  private String playerName;
  private Map<Integer, Hat> playerHats = new HashMap<>();

  public HatPlayer(final UUID playerUUID, final String playerName, final HashMap<Integer, Hat> playerHats) {
    this(playerUUID, playerName);
    this.playerHats = playerHats;
  }

  public HatPlayer(final UUID playerUUID, final String playerName) {
    this.playerUUID = playerUUID;
    this.playerName = playerName;
  }

  public boolean addHat(final Hat hat, final boolean sendEvent, final boolean saveToDB) {
    if (playerHats.containsKey(hat.getHatID())) {
      final Hat playerHat = playerHats.get(hat.getHatID());
      if (playerHat.isFlaggedForRemoval()) {
        playerHat.unflagForRemoval();
        if (saveToDB) {
          Bukkit.getScheduler().runTaskAsynchronously(DreamHats.getInstance(), () -> DreamHats.getInstance().getHatDAO().savePlayerHat(getPlayerName(), hat));
        }
        if (sendEvent) {
          Bukkit.getScheduler().runTask(DreamHats.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new HatAddEvent(getBukkitPlayer(), hat)));
        }
        return true;
      }
    } else {
      playerHats.put(hat.getHatID(), hat);
      if (saveToDB) {
        Bukkit.getScheduler().runTaskAsynchronously(DreamHats.getInstance(), () -> DreamHats.getInstance().getHatDAO().savePlayerHat(getPlayerName(), hat));
      }
      if (sendEvent) {
        Bukkit.getScheduler().runTask(DreamHats.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new HatAddEvent(getBukkitPlayer(), hat)));
      }
      return true;
    }
    return false;
  }

  public boolean removeHat(final Hat hat, final boolean sendEvent) {
    if (playerHats.containsKey(hat.getHatID())) {
      playerHats.get(hat.getHatID()).flagForRemoval();
      Bukkit.getScheduler().runTaskAsynchronously(DreamHats.getInstance(), () -> DreamHats.getInstance().getHatDAO().removeHatFromPlayer(getPlayerName(), hat));
      if (sendEvent) {
        Bukkit.getScheduler().runTask(DreamHats.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new HatRemoveEvent(getBukkitPlayer(), hat)));
      }
      return true;
    }
    return false;
  }

  public boolean hasHat(final Hat hat) {
    final Hat playerHat = playerHats.get(hat.getHatID());
    return playerHat != null && !playerHat.isFlaggedForRemoval();
  }

  public UUID getPlayerUUID() {
    return playerUUID;
  }

  public String getPlayerName() {
    return playerName;
  }

  public Collection<Hat> getHats() {
    return playerHats.values();
  }

  public Player getBukkitPlayer() {
    return Bukkit.getPlayer(playerUUID);
  }
}
