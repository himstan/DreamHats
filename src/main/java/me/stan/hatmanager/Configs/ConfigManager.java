package me.stan.hatmanager.Configs;


import me.stan.hatmanager.Configs.SubConfigs.DataBaseConfig;
import me.stan.hatmanager.Configs.SubConfigs.DefaultConfig;
import me.stan.hatmanager.Configs.SubConfigs.HatConfig;
import me.stan.hatmanager.HatManager;

public class ConfigManager {

    private HatManager plugin;
    private DataBaseConfig dataBaseConfig;
    private HatConfig hatConfig;
    private DefaultConfig defaultConfig;

    public ConfigManager(HatManager plugin) {
        this.plugin = plugin;
        this.dataBaseConfig = new DataBaseConfig(plugin, "dbconfig");
        this.hatConfig = new HatConfig(plugin, "hats");
        this.defaultConfig = new DefaultConfig(plugin, "config");
    }

    public DefaultConfig getDefaultConfig() { return defaultConfig; }

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public HatConfig getHatConfig() { return hatConfig; }
}
