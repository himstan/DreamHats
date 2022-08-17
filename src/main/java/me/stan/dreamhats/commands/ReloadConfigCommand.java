package me.stan.dreamhats.commands;

import hu.stan.dreamcore.commands.SubCommand;
import me.stan.dreamhats.DreamHats;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;

public class ReloadConfigCommand extends SubCommand {

  public ReloadConfigCommand(final DreamHats plugin, final String commandName, final String commandDescription, final String commandUsage, final Permission permission) {
    super(plugin, commandName, commandDescription, commandUsage, permission);
  }

  @Override
  public boolean execute(final Player player, final String commandName, final String label, final String[] args) {
    DreamHats.getInstance().getConfigManager().reloadConfigs();
    return true;
  }

  @Override
  public List<String> tabComplete(final Player player, final String label, final String[] args) {
    return Collections.emptyList();
  }
}
