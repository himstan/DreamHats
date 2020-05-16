package me.stan.hatmanager.Commands.SubCommands;

import me.stan.hatmanager.Commands.SubCommand;
import me.stan.hatmanager.HatManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;

public class ReloadConfigCommand extends SubCommand {

    public ReloadConfigCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        super(plugin, commandName, commandDescription, commandUsage, permission, commandAliases);
    }

    public ReloadConfigCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        super(plugin, commandName, commandDescription, commandUsage, permission);
    }

    public ReloadConfigCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
        super(plugin, commandName, commandDescription, commandUsage);
    }

    @Override
    public boolean execute(Player player, String commandName, String label, String[] args) {
        getPlugin().getConfigManager().getDefaultConfig().reloadConfig();
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String label, String[] args) {
        return Collections.emptyList();
    }
}
