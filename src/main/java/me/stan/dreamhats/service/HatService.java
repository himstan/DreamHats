package me.stan.dreamhats.service;

import hu.stan.dreamcore.configs.subconfigs.SubConfig;
import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.dao.HatDAO;
import me.stan.dreamhats.extractor.HatExtractor;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatGroup;
import me.stan.dreamhats.model.HatPlayer;
import me.stan.dreamhats.module.PlayerModule;
import me.stan.dreamhats.utils.HatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HatService {

  private final HatDAO hatDAO;
  private final DreamHats plugin;
  private final SubConfig subConfig;
  private final Map<Integer, Hat> hats = new HashMap<>();
  private final Map<String, Integer> hatNameToID = new HashMap<>();
  private final Map<String, HatGroup> hatGroupMap = new HashMap<>();
  private final PlayerModule playerModule;

  public HatService(final DreamHats plugin) {
    this.plugin = plugin;
    this.subConfig = plugin.getConfigManager().getSubConfig("config");
    hatDAO = plugin.getHatDAO();
    playerModule = plugin.getModuleManager().getModule(PlayerModule.class);
    if (setupHats()) {
      plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Hats loaded successfully!");
    }
  }

  public boolean setupHats() {
    final HatExtractor hatExtractor = plugin.getHatExtractor();
    for (final Hat hat : hatExtractor.getAllHats()) {
      hatNameToID.put(hat.getLookupName().toLowerCase(), hat.getHatID());
      hats.put(hat.getHatID(), hat);
    }
    for (final HatGroup hatGroup : hatExtractor.getAllHatGroups(this)) {
      hatGroupMap.put(hatGroup.getHatGroupPermName().toLowerCase(), hatGroup);
    }
    return true;
  }

  public void giveHatToOffline(final String playerName, final Hat hat) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> hatDAO.giveHatToOffline(playerName, hat));
  }

  public boolean giveHat(final Player player, final Hat hat, final boolean sendEvent, final boolean playSound) {
    final HatPlayer hatPlayer = playerModule.getHatPlayer(player);
    if (hatPlayer == null) {
      return false;
    }
    if (playSound) {
      playEventSound(player, Sound.valueOf(subConfig.getConfig().getString("add.soundeffect")),
          subConfig.getConfig().getBoolean("add.globalsound"));
    }
    return hatPlayer.addHat(new Hat(hat), sendEvent, true);
  }

  public boolean giveHatGroup(final Player player, final String hatGroupName) {
    return giveHatGroup(player, getHatGroup(hatGroupName));
  }

  public boolean giveHatGroup(final Player player, final HatGroup hatGroup) {
    if (hatGroup == null) {
      return false;
    }
    final Collection<Hat> hats = hatGroup.getHats();
    for (final Hat hat : hats) {
      giveHat(player, hat, true, false);
    }
    playEventSound(player, Sound.valueOf(subConfig.getConfig().getString("add.soundeffect")),
        subConfig.getConfig().getBoolean("add.globalsound"));
    return true;
  }

  public boolean equipHat(final Player player, final Hat hat, final boolean overrideHat) {
    if (!player.hasPermission(subConfig.getConfig().getString("permissions.hat_equip"))) {
      player.sendRawMessage(
          TranslationService.translate("permission_messages.equip_error", player));
      return false;
    }
    if (!overrideHat) {
      final ItemStack playerHelmet = player.getInventory().getHelmet();
      if (playerHelmet != null) {
        if (!playerHelmet.getType().equals(Material.STICK)) {
          player.sendRawMessage(
              TranslationService.translate("error.hat_already_equipped", player));
          return false;
        } else {
          if (Objects.requireNonNull(playerHelmet.getItemMeta()).getCustomModelData() == hat.getHatModelData()) {
            return false;
          }
        }
        dequipHat(player);
      }
    }
    final HatPlayer hatPlayer = playerModule.getHatPlayer(player);
    if (!hatPlayer.hasHat(hat)) {
      player.sendRawMessage(
          TranslationService.translate("error.hat_you_dont_have", player));
      return false;
    }
    final ItemStack hatItem = HatUtils.createHatItem(hat);
    player.getInventory().setHelmet(hatItem);
    playEventSound(player, Sound.valueOf(subConfig.getConfig().getString("equip.soundeffect")),
        subConfig.getConfig().getBoolean("equip.globalsound"));
    return true;
  }

  public boolean dequipHat(final Player player) {
    if (player != null) {
      if (hasHatEquipped(player)) {
        player.getInventory().setHelmet(null);
        playEventSound(player, Sound.valueOf(subConfig.getConfig().getString("dequip.soundeffect")),
            subConfig.getConfig().getBoolean("dequip.globalsound"));
        return true;
      }
    }
    return false;
  }

  public boolean removeHatFromOffline(final String playerName, final Hat hat) {
    return hatDAO.removeHatFromPlayer(playerName, hat);
  }

  public boolean removeHat(final Player player, final Hat hat, final boolean sendEvent) {
    if (player == null) {
      return false;
    }
    final HatPlayer hatPlayer = playerModule.getHatPlayer(player);
    if (hatPlayer.hasHat(hat)) {
      hatPlayer.removeHat(hat, sendEvent);
      if (hasHatEquipped(player, hat)) {
        dequipHat(player);
      }
      playEventSound(player, Sound.valueOf(subConfig.getConfig().getString("remove.soundeffect")),
          subConfig.getConfig().getBoolean("remove.globalsound"));
      return true;
    }
    return false;
  }

  public boolean hasHatEquipped(final Player player, final Hat hat) {
    final ItemStack playerHelmet = player.getInventory().getHelmet();
    return playerHelmet != null && playerHelmet.getType().equals(Material.STICK) && Objects.requireNonNull(playerHelmet.getItemMeta()).getCustomModelData() == hat.getHatModelData();
  }

  public boolean hasHatEquipped(final Player player) {
    final ItemStack playerHelmet = player.getInventory().getHelmet();
    return playerHelmet != null && playerHelmet.getType().equals(Material.STICK);
  }

  public List<Hat> getHatCollection(final List<String> hatNames) {
    final List<Hat> hatList = new ArrayList<>();
    for (final String hatName : hatNames) {
      final Hat hat = getHat(hatName);
      if (hat != null) {
        hatList.add(hat);
      }
    }
    return hatList;
  }

  public Hat getHat(final int hatID) {
    return hats.get(hatID);
  }

  public Hat getHat(final String hatName) {
    if (hatNameToID.containsKey(hatName.toLowerCase())) {
      final int hatID = hatNameToID.get(hatName.toLowerCase());
      return getHat(hatID);
    }
    return null;
  }

  public HatGroup getHatGroup(final String hatGroupName) {
    for (final String keys : hatGroupMap.keySet()) {
      System.out.println(keys);
    }
    return hatGroupMap.get(hatGroupName);
  }

  public Collection<Hat> getAllHats() {
    return hats.values();
  }

  private void playEventSound(final Player player, final Sound sound, final boolean global) {
    if (global) {
      player.getWorld().playSound(player.getLocation(), sound, 1, 1);
    } else {
      player.playSound(player.getLocation(), sound, 1, 1);
    }
  }

}
