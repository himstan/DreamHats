package me.stan.hatmanager.Configs.SubConfigs;

import me.stan.hatmanager.HatManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SubConfig {

    protected String configName;
    private File file;
    protected FileConfiguration config;
    protected HatManager plugin;

    public SubConfig(HatManager plugin, String configName) {
        this.plugin = plugin;
        this.configName = configName;
        setupConfig();
    }

    private void setupConfig(){
        plugin.saveResource(configName + ".yml",false);
        file = new File(plugin.getDataFolder(), configName + ".yml");
        if (!file.exists()){
            try{
                file.createNewFile();
            }catch (IOException e){
                plugin.getLogger().warning("Couldn't read " + configName + ".yml");
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public void saveConfig(){
        try{
            config.save(file);
        }catch (IOException e){
            plugin.getLogger().warning("Couldn't save " + configName + ".yml");
        }
        reloadConfig();
    }

    public void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(file);
    }
}
