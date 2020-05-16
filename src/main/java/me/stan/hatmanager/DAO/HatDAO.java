package me.stan.hatmanager.DAO;

import me.stan.hatmanager.Model.Hat;
import me.stan.hatmanager.Model.HatPlayer;
import org.bukkit.entity.Player;

public interface HatDAO {

    public boolean savePlayerHats(HatPlayer player);

    boolean savePlayerHat(String playerName, Hat hat);

    public HatPlayer loadPlayerHats(Player player);

    public boolean removeHatFromPlayer(String playerName, Hat hat);

    public boolean giveHatToOffline(String playerName, Hat hat);

}
