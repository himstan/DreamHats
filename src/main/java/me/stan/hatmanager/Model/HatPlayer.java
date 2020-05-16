package me.stan.hatmanager.Model;

import me.stan.hatmanager.Events.HatAddEvent;
import me.stan.hatmanager.Events.HatRemoveEvent;
import me.stan.hatmanager.HatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class HatPlayer {
    private UUID playerUUID;
    private String playerName;
    private Map<Integer, Hat> playerHats = new HashMap<>();

    public HatPlayer(UUID playerUUID, String playerName, HashMap<Integer, Hat> playerHats) {
        this(playerUUID,playerName);
        this.playerHats = playerHats;
    }

    public HatPlayer(UUID playerUUID, String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
    }

    public boolean addHat(Hat hat, boolean sendEvent, boolean saveToDB) {
        if (playerHats.containsKey(hat.getHatID())) {
            Hat playerHat = playerHats.get(hat.getHatID());
            if (playerHat.isFlaggedForRemoval()) {
                playerHat.unflagForRemoval();
                if (saveToDB) {
                    Bukkit.getScheduler().runTaskAsynchronously(HatManager.getInstance(), () -> HatManager.getInstance().getHatDAO().savePlayerHat(getPlayerName(), hat));
                }
                if (sendEvent) {
                    Bukkit.getScheduler().runTask(HatManager.getInstance(),() -> Bukkit.getServer().getPluginManager().callEvent(new HatAddEvent(getBukkitPlayer(), hat)));
                }
                return true;
            }
        } else {
            playerHats.put(hat.getHatID(),hat);
            if (saveToDB) {
                Bukkit.getScheduler().runTaskAsynchronously(HatManager.getInstance(), () -> HatManager.getInstance().getHatDAO().savePlayerHat(getPlayerName(), hat));
            }
            if (sendEvent) {
                Bukkit.getScheduler().runTask(HatManager.getInstance(),() -> Bukkit.getServer().getPluginManager().callEvent(new HatAddEvent(getBukkitPlayer(), hat)));
            }
            return true;
        }
        return false;
    }

    public boolean removeHat(Hat hat, boolean sendEvent) {
        if (playerHats.containsKey(hat.getHatID())) {
            playerHats.get(hat.getHatID()).flagForRemoval();
            Bukkit.getScheduler().runTaskAsynchronously(HatManager.getInstance(), () -> HatManager.getInstance().getHatDAO().removeHatFromPlayer(getPlayerName(),hat));
            if (sendEvent) {
                Bukkit.getScheduler().runTask(HatManager.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(new HatRemoveEvent(getBukkitPlayer(), hat)));
            }
            return true;
        }
        return false;
    }

    public boolean hasHat(Hat hat) {
        Hat playerHat = playerHats.get(hat.getHatID());
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
