package me.stan.hatmanager.Handlers;

import me.stan.hatmanager.DAO.HatDAO;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.HatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class HatPlayerManager implements Listener {

    private final HatManager plugin;
    private final HatDAO hatDAO;
    private final Map<String, HatPlayer> onlineHatPlayers = new HashMap<>();

    public HatPlayerManager(HatManager plugin) {
        this.plugin = plugin;
        hatDAO = plugin.getHatDAO();
    }

    public void loadAllOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayer(player);
        }
    }

    private void addPlayer(Player player) {
        HatPlayer hatPlayer = plugin.getHatDAO().loadPlayerHats(player);
        if (hatPlayer == null) {
            hatPlayer = new HatPlayer(player.getUniqueId(),player.getName());
        }
        onlineHatPlayers.put(player.getName().toLowerCase(),hatPlayer);
    }

    private void removePlayer(Player player) {
        HatPlayer hatPlayer = getHatPlayer(player);
        onlineHatPlayers.remove(player.getName().toLowerCase());
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin,() -> hatDAO.savePlayerHats(hatPlayer));
    }

    public HatPlayer getHatPlayer(Player player) {
        return onlineHatPlayers.get(player.getName().toLowerCase());
    }

    public Set<HatPlayer> getAllPlayers() {
        return new LinkedHashSet<>(onlineHatPlayers.values());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin,() -> addPlayer(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        removePlayer(player);
    }

    @EventHandler
    public void onHatClick(InventoryClickEvent e) {
        int helmetSlot = 5;
        if (e.getRawSlot() == helmetSlot) {
            if (Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.STICK)) {
                Player player = (Player) e.getWhoClicked();
                plugin.getHatHandler().dequipHat(player);
                e.setCancelled(true);
            }
        }
    }

}
