package me.stan.hatmanager.Commands;

import me.stan.hatmanager.HatManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.List;

public abstract class SubCommand {
    private HatManager plugin;
    private String commandName;
    private String commandDescription;
    private String commandUsage;
    private List<String> commandAliases;
    private Permission permission;

    public SubCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.commandAliases = commandAliases;
    }

    public SubCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandUsage = commandUsage;
        this.permission = permission;
        this.commandAliases = null;
    }

    public SubCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandUsage = commandUsage;
        this.permission = null;
        this.commandAliases = null;
    }

    public abstract boolean execute(Player player, String commandName, String label, String[] args);
    public abstract List<String> tabComplete(Player player, String label, String[] args);

    public String aliasesToString() {
        if (commandAliases == null || commandAliases.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < commandAliases.size(); i++) {
            builder.append(commandAliases.get(i));
            if (i + 1 != commandAliases.size()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public HatManager getPlugin() {
        return plugin;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public String getCommandUsage() {
        return commandUsage;
    }

    public List<String> getCommandAliases() {
        return commandAliases;
    }

    public Permission getPermission() {
        return permission;
    }
}
