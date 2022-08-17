package me.stan.dreamhats.extractor;

import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatGroup;
import me.stan.dreamhats.service.HatService;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HatExtractor {

  private final DreamHats plugin;

  public HatExtractor(final DreamHats plugin) {
    this.plugin = plugin;
  }

  public List<Hat> getAllHats() {
    final var config = plugin.getConfigManager().getSubConfig("hats").getConfig();
    final List<Hat> hatList = new ArrayList<>();
    final ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("hats"));
    final Set<String> keys = section.getKeys(false);
    for (final String key : keys) {
      final int hatID = section.getInt(key + ".id");
      final String hatLookup = key;
      final String hatName = section.getString(key + ".name");
      final int hatModelData = section.getInt(key + ".modelData");
      hatList.add(new Hat(hatID, hatLookup, hatName, hatModelData));
    }
    return hatList;
  }

  public List<HatGroup> getAllHatGroups(final HatService hatService) {
    final var config = plugin.getConfigManager().getSubConfig("hats").getConfig();
    final List<HatGroup> hatGroupList = new ArrayList<>();
    final ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("hatgroups"));
    final Set<String> keys = section.getKeys(false);
    for (final String key : keys) {
      final String groupName = section.getString(key + ".name");
      final String permGroupName = section.getString(key + ".permission_group_name");
      final List<String> hatNames = section.getStringList(key + ".hats");
      final List<Hat> hats = hatService.getHatCollection(hatNames);
      hatGroupList.add(new HatGroup(groupName, permGroupName, hats));
    }
    return hatGroupList;
  }
}
