package me.stan.dreamhats.dao;

import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatPlayer;
import org.bukkit.entity.Player;

public interface HatDAO {

  boolean savePlayerHats(HatPlayer player);

  boolean savePlayerHat(String playerName, Hat hat);

  HatPlayer loadPlayerHats(Player player);

  boolean removeHatFromPlayer(String playerName, Hat hat);

  boolean giveHatToOffline(String playerName, Hat hat);

}
