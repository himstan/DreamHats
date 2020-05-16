package me.stan.hatmanager.Configs.SubConfigs;

import me.stan.hatmanager.Handlers.HatHandler;
import me.stan.hatmanager.Model.Hat;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.HatGroup;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class HatConfig extends SubConfig{

    public HatConfig(HatManager plugin, String configName) {
        super(plugin, configName);
    }

    public List<Hat> getAllHats() {
        List<Hat> hatList = new ArrayList<>();
        ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("hats"));
        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            int hatID = section.getInt(key+".id");
            String hatLookup = key;
            String hatName = section.getString(key+".name");
            int hatModelData = section.getInt(key+".modelData");
            hatList.add(new Hat(hatID, hatLookup, hatName, hatModelData));
        }
        return hatList;
    }

    public List<HatGroup> getAllHatGroups(HatHandler hatHandler) {
        List<HatGroup> hatGroupList = new ArrayList<>();
        ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection("hatgroups"));
        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            String groupName = section.getString(key + ".name");
            String permGroupName = section.getString(key + ".permission_group_name");
            List<String> hatNames = section.getStringList(key + ".hats");
            List<Hat> hats = hatHandler.getHatCollection(hatNames);
            hatGroupList.add(new HatGroup(groupName, permGroupName, hats));
        }
        return hatGroupList;
    }
}
