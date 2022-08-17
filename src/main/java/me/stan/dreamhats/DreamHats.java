package me.stan.dreamhats;

import hu.stan.dreamcore.DreamPlugin;
import lombok.Getter;
import me.stan.dreamhats.LuckPerms.Events;
import me.stan.dreamhats.commands.AddHatCommand;
import me.stan.dreamhats.commands.EquipHatCommand;
import me.stan.dreamhats.commands.ReloadConfigCommand;
import me.stan.dreamhats.commands.RemoveHatCommand;
import me.stan.dreamhats.dao.HatDAO;
import me.stan.dreamhats.dao.SQLHatDAO;
import me.stan.dreamhats.dao.YMLHatDao;
import me.stan.dreamhats.extractor.HatExtractor;
import me.stan.dreamhats.service.HatService;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;

@Getter
public final class DreamHats extends DreamPlugin {

  private static DreamHats instance;
  private HatService hatService;
  private HatDAO hatDAO;
  private HatExtractor hatExtractor;

  public static DreamHats getInstance() {
    return instance;
  }

  @Override
  public void onPluginEnable() {
    instance = this;
    initializeConfigs();
    hatExtractor = new HatExtractor(this);
    initializeSaving();
    hatService = new HatService(this);
    loadLuckPermsAPI();
    getModuleManager().enableModules();
    registerCommandManager("hat")
        .addSubCommands(
            new AddHatCommand(this, "add", "Gives a hat to a player", "add <PlayerName> <Hat ID/Name>", getPermissionFromConfig("permissions.hat_add")),
            new RemoveHatCommand(this, "remove", "Removes hat from a player", "remove <PlayerName> <Hat ID/Name>", getPermissionFromConfig("permissions.hat_remove")),
            new EquipHatCommand(this, "equip", "Equips a hat", "equip <Hat ID/Name>", getPermissionFromConfig("permissions.hat_equip")),
            new ReloadConfigCommand(this, "reloadconfig", "Reloads the messages config", "reloadconfig", getPermissionFromConfig("permissions.reload_config"))
        );
  }

  @Override
  public void onPluginDisable() {
    getModuleManager().disableModules();
  }

  @Override
  public void onPluginLoad() {

  }

  private void initializeConfigs() {
    getConfigManager().addSubConfigList("config", "dbconfig", "hats", "save");
  }

  private void initializeSaving() {
    if (isSqlEnabled()) {
      hatDAO = new SQLHatDAO(this);
    } else {
      hatDAO = new YMLHatDao(this);
    }
  }

  private boolean isSqlEnabled() {
    return getConfigManager().getSubConfig("dbconfig").getConfig().getBoolean("enabled");
  }

  private void loadLuckPermsAPI() {
    if (Bukkit.getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
      try {
        final LuckPerms api = LuckPermsProvider.get();
        new Events(this, api);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[" + getName() + "] Hooked into LuckPerms! Listening for Promote Event...");
      } catch (final Exception ignored) {
      }
    }
  }

  private Permission getPermissionFromConfig(final String permission) {
    return new Permission(getConfigManager().getSubConfig("config").getConfig().getString(permission, ""));
  }
}
