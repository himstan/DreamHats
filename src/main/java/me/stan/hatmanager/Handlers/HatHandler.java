package me.stan.hatmanager.Handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stan.hatmanager.Configs.SubConfigs.DefaultConfig;
import me.stan.hatmanager.Configs.SubConfigs.HatConfig;
import me.stan.hatmanager.DAO.HatDAO;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.Hat;
import me.stan.hatmanager.Model.HatGroup;
import me.stan.hatmanager.Model.HatPlayer;
import me.stan.hatmanager.Utils.HatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HatHandler {

    private final DefaultConfig defaultConfig;
    private final HatDAO hatDAO;
    private final HatManager plugin;
    private final Map<Integer, Hat> hats = new HashMap<>();
    private final Map<String, Integer> hatNameToID = new HashMap<>();
    private final Map<String, HatGroup> hatGroupMap = new HashMap<>();
    private final HatPlayerManager hatPlayerManager;

    public HatHandler(HatManager plugin) {
        this.plugin = plugin;
        hatDAO = plugin.getHatDAO();
        hatPlayerManager = plugin.getHatPlayerManager();
        defaultConfig = plugin.getConfigManager().getDefaultConfig();
        if (setupHats()) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Hats loaded successfully!");
        }
    }

    public boolean setupHats() {
        HatConfig hatConfig = plugin.getConfigManager().getHatConfig();
        for (Hat hat : hatConfig.getAllHats()) {
            hatNameToID.put(hat.getLookupName().toLowerCase(),hat.getHatID());
            hats.put(hat.getHatID(), hat);
        }
        for (HatGroup hatGroup : hatConfig.getAllHatGroups(this)) {
            hatGroupMap.put(hatGroup.getHatGroupPermName().toLowerCase(), hatGroup);
        }
        return true;
    }

    public void giveHatToOffline(String playerName, Hat hat) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> hatDAO.giveHatToOffline(playerName, hat));
    }

    public boolean giveHat(Player player, Hat hat, boolean sendEvent, boolean playSound) {
        HatPlayer hatPlayer = hatPlayerManager.getHatPlayer(player);
        if (hatPlayer == null) {
            return false;
        }
        if (playSound) {
            playEventSound(player, Sound.valueOf(defaultConfig.getAddSoundEffect()), defaultConfig.isAddSoundEffectGlobal());
        }
        return hatPlayer.addHat(new Hat(hat), sendEvent, true);
    }

    public boolean giveHatGroup(Player player, String hatGroupName) {
        return giveHatGroup(player, getHatGroup(hatGroupName));
    }

    public boolean giveHatGroup(Player player, HatGroup hatGroup) {
        if (hatGroup == null) {
            return false;
        }
        Collection<Hat> hats = hatGroup.getHats();
        for (Hat hat : hats) {
            giveHat(player, hat, true, false);
        }
        playEventSound(player, Sound.valueOf(defaultConfig.getAddSoundEffect()), defaultConfig.isAddSoundEffectGlobal());
        return true;
    }

    public boolean equipHat(Player player, Hat hat, boolean overrideHat) {
        if (!player.hasPermission(defaultConfig.getHatEquipPerm())) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getNoEquipPermError()));
            return false;
        }
        if (!overrideHat) {
            ItemStack playerHelmet = player.getInventory().getHelmet();
            if (playerHelmet != null) {
                if (!playerHelmet.getType().equals(Material.STICK)) {
                    player.sendRawMessage(defaultConfig.getHatAlreadyEquippedMessage());
                    return false;
                } else {
                    if (Objects.requireNonNull(playerHelmet.getItemMeta()).getCustomModelData() == hat.getHatModelData()) {
                        return false;
                    }
                }
                dequipHat(player);
            }
        }
        HatPlayer hatPlayer = hatPlayerManager.getHatPlayer(player);
        if (!hatPlayer.hasHat(hat)) {
            player.sendRawMessage(defaultConfig.getHatDontHaveMessage());
            return false;
        }
        ItemStack hatItem = HatUtils.createHatItem(hat);
        player.getInventory().setHelmet(hatItem);
        playEventSound(player, Sound.valueOf(defaultConfig.getEquipSoundEffect()), defaultConfig.isEquipSoundEffectGlobal());
        return true;
    }

    public boolean dequipHat(Player player) {
        if (player != null) {
            if (hasHatEquipped(player)) {
                player.getInventory().setHelmet(null);
                playEventSound(player, Sound.valueOf(defaultConfig.getDequipSoundEffect()), defaultConfig.isDequipSoundEffectGlobal());
                return true;
            }
        }
        return false;
    }

    public boolean removeHatFromOffline(String playerName, Hat hat) {
        return hatDAO.removeHatFromPlayer(playerName, hat);
    }

    public boolean removeHat(Player player, Hat hat, boolean sendEvent) {
        if (player == null) {
            return false;
        }
        HatPlayer hatPlayer = hatPlayerManager.getHatPlayer(player);
        if (hatPlayer.hasHat(hat)) {
            hatPlayer.removeHat(hat, sendEvent);
            if (hasHatEquipped(player,hat)) {
                dequipHat(player);
            }
            playEventSound(player, Sound.valueOf(defaultConfig.getRemoveSoundEffect()),defaultConfig.isRemoveSoundEffectGlobal());
            return true;
        }
        return false;
    }

    public boolean hasHatEquipped(Player player, Hat hat) {
        ItemStack playerHelmet = player.getInventory().getHelmet();
        return playerHelmet != null && playerHelmet.getType().equals(Material.STICK) && Objects.requireNonNull(playerHelmet.getItemMeta()).getCustomModelData() == hat.getHatModelData();
    }

    public boolean hasHatEquipped(Player player) {
        ItemStack playerHelmet = player.getInventory().getHelmet();
        return playerHelmet != null && playerHelmet.getType().equals(Material.STICK);
    }

    public List<Hat> getHatCollection(List<String> hatNames) {
        List<Hat> hatList = new ArrayList<>();
        for (String hatName : hatNames) {
            Hat hat = getHat(hatName);
            if (hat != null) hatList.add(hat);
        }
        return hatList;
    }

    public Hat getHat(int hatID) {
        return hats.get(hatID);
    }

    public Hat getHat(String hatName) {
        if (hatNameToID.containsKey(hatName.toLowerCase())) {
            int hatID = hatNameToID.get(hatName.toLowerCase());
            return getHat(hatID);
        }
        return null;
    }

    public HatGroup getHatGroup(String hatGroupName) {
        System.out.println("Trying to get: " + hatGroupName + " out of these keys.");
        for (String keys : hatGroupMap.keySet()) {
            System.out.println(keys);
        }
        return hatGroupMap.get(hatGroupName);
    }

    public Collection<Hat> getAllHats() {
        return hats.values();
    }

    private void playEventSound(Player player, Sound sound, boolean global) {
        if (global) {
            player.getWorld().playSound(player.getLocation(),sound,1,1);
        } else {
            player.playSound(player.getLocation(),sound,1,1);
        }
    }

}
