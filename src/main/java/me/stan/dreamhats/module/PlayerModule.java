package me.stan.dreamhats.module;

import hu.stan.dreamcore.DreamPlugin;
import hu.stan.dreamcore.annotation.DreamModule;
import hu.stan.dreamcore.module.Module;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.dao.HatDAO;
import me.stan.dreamhats.model.HatPlayer;
import me.stan.dreamhats.service.HatService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

@DreamModule
public class PlayerModule extends Module {

  private final Map<String, HatPlayer> onlineHatPlayers = new HashMap<>();
  private HatDAO hatDAO;
  private HatService hatService;

  public PlayerModule(final DreamPlugin plugin) {
    super(plugin);
  }

  public HatPlayer getHatPlayer(final Player player) {
    return onlineHatPlayers.get(player.getName().toLowerCase());
  }

  public Set<HatPlayer> getAllPlayers() {
    return new LinkedHashSet<>(onlineHatPlayers.values());
  }

  @Override
  protected void onEnable() {
    hatDAO = DreamHats.getInstance().getHatDAO();
    hatService = DreamHats.getInstance().getHatService();
    loadAllOnlinePlayers();
  }

  @Override
  protected void onDisable() {
    for (final HatPlayer player : getAllPlayers()) {
      if (player != null) {
        hatDAO.savePlayerHats(player);
      }
    }
  }

  private void loadAllOnlinePlayers() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      addPlayer(player);
    }
  }

  private void addPlayer(final Player player) {
    HatPlayer hatPlayer = hatDAO.loadPlayerHats(player);
    if (hatPlayer == null) {
      hatPlayer = new HatPlayer(player.getUniqueId(), player.getName());
    }
    onlineHatPlayers.put(player.getName().toLowerCase(), hatPlayer);
  }

  private void removePlayer(final Player player) {
    final HatPlayer hatPlayer = getHatPlayer(player);
    onlineHatPlayers.remove(player.getName().toLowerCase());
    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> hatDAO.savePlayerHats(hatPlayer));
  }

  @EventHandler
  private void onJoin(final PlayerJoinEvent e) {
    final Player player = e.getPlayer();
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> addPlayer(player));
  }

  @EventHandler
  private void onQuit(final PlayerQuitEvent e) {
    final Player player = e.getPlayer();
    removePlayer(player);
  }

  @EventHandler
  private void onHatClick(final InventoryClickEvent e) {
    if (isHelmetClick(e)) {
      final Player player = (Player) e.getWhoClicked();
      hatService.dequipHat(player);
      e.setCancelled(true);
    }
  }

  private boolean isHelmetClick(final InventoryClickEvent event) {
    final var clickedInventory = event.getClickedInventory();
    final var currentItem = event.getCurrentItem();
    return Objects.nonNull(clickedInventory) && Objects.nonNull(currentItem)
        && clickedInventory.getType().equals(InventoryType.PLAYER)
        && event.getSlotType() == InventoryType.SlotType.ARMOR
        && currentItem.getType().equals(Material.STICK);
  }
}
