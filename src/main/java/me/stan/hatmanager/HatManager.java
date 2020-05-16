package me.stan.hatmanager;

import me.stan.hatmanager.Commands.HatCommandManager;
import me.stan.hatmanager.Configs.ConfigManager;
import me.stan.hatmanager.DAO.HatDAO;
import me.stan.hatmanager.DAO.IHatDAO;
import me.stan.hatmanager.Handlers.HatHandler;
import me.stan.hatmanager.Handlers.HatPlayerManager;
import me.stan.hatmanager.LuckPerms.Events;
import me.stan.hatmanager.Model.HatPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HatManager extends JavaPlugin {


    private boolean isLuckPermsEnabled;
    private ConfigManager configManager;
    private HatHandler hatHandler;
    private HatDAO hatDAO;
    private HatPlayerManager hatPlayerManager;
    private HatCommandManager hatCommandManager;
    private static HatManager instance;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        hatDAO = new IHatDAO(this);
        hatPlayerManager = new HatPlayerManager(this);
        hatHandler = new HatHandler(this);
        loadLuckPermsAPI();
        getServer().getPluginManager().registerEvents(hatPlayerManager, this);
        hatCommandManager = new HatCommandManager(this);
        Objects.requireNonNull(getCommand("hats")).setExecutor(hatCommandManager);
        Objects.requireNonNull(getCommand("hats")).setTabCompleter(hatCommandManager);
        hatPlayerManager.loadAllOnlinePlayers();
    }

    private void loadLuckPermsAPI() {
        try {
            LuckPerms api = LuckPermsProvider.get();
            new Events(this,api);
            isLuckPermsEnabled = true;
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() +  "] Hooked into LuckPerms! Listening for Promote Event...");
        } catch (IllegalStateException e) {
            isLuckPermsEnabled = false;
        }
    }

    @Override
    public void onDisable() {
        if (hatPlayerManager != null) {
            for (HatPlayer player : hatPlayerManager.getAllPlayers()) {
                if (player != null) {
                    hatDAO.savePlayerHats(player);
                }
            }
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HatHandler getHatHandler() {
        return hatHandler;
    }

    public HatDAO getHatDAO() {
        return hatDAO;
    }

    public HatPlayerManager getHatPlayerManager() {
        return hatPlayerManager;
    }

    public HatCommandManager getHatCommandManager() {
        return hatCommandManager;
    }

    public static HatManager getInstance() {
        return instance;
    }
}
