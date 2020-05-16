package me.stan.hatmanager.Commands.SubCommands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stan.hatmanager.Commands.SubCommand;
import me.stan.hatmanager.Configs.SubConfigs.DefaultConfig;
import me.stan.hatmanager.Handlers.HatHandler;
import me.stan.hatmanager.Handlers.HatPlayerManager;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.Hat;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveHatCommand extends SubCommand {

    private HatHandler hatHandler;
    private HatPlayerManager hatPlayerManager;

    public RemoveHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        super(plugin, commandName, commandDescription, commandUsage, permission, commandAliases);
        init();
    }

    public RemoveHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        super(plugin, commandName, commandDescription, commandUsage, permission);
        init();
    }

    public RemoveHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
        super(plugin, commandName, commandDescription, commandUsage);
        init();
    }

    private void init() {
        hatHandler = getPlugin().getHatHandler();
        hatPlayerManager = getPlugin().getHatPlayerManager();
    }

    @Override
    public boolean execute(Player player, String commandName, String label, String[] args) {
        DefaultConfig defaultConfig = getPlugin().getConfigManager().getDefaultConfig();
        if (player == null) {
            return true;
        }
        if (args.length == 0) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getEnterUsername()));
            return true;
        }
        if (args.length == 1) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getEnterHatNameOrID()));
            return true;
        }
        if (args.length > 2) {
            player.sendRawMessage("&4Usage: /"+label+ " " + getCommandUsage());
            return true;
        }
        boolean success = false;
        boolean online = false;
        String removeFromName = args[0];
        String hatID = args[1];
        Player removeFrom = getPlugin().getServer().getPlayer(removeFromName);
        Hat hat = hatHandler.getHat(hatID);
        if (hat == null) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(removeFrom,defaultConfig.getHatNotFound().replace("%hat_name%",hatID)));
            return true;
        }
        if (removeFrom != null) {
            if (!hatPlayerManager.getHatPlayer(removeFrom).hasHat(hat)) {
                player.sendRawMessage(PlaceholderAPI.setPlaceholders(removeFrom,defaultConfig.getHatPlayerDoesntHave().replace("%hat_name%",hatID)));
                return true;
            }
            success = hatHandler.removeHat(removeFrom, hat,true);
            online = true;
        } else {
            success = hatHandler.removeHatFromOffline(removeFromName, hat);
        }
        if (success) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(removeFrom,defaultConfig.getHatRemove().replace("%hat_name%",hat.getHatName())));
            if (online) {
                removeFrom.sendRawMessage(PlaceholderAPI.setPlaceholders(removeFrom,defaultConfig.getHatRemoved().replace("%hat_name%",hat.getHatName())));
            }
        }
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String label, String[] args) {
        if (args.length == 1) {
            return null;
        } else if (args.length == 2) {
            return hatHandler.getAllHats().stream().map(Hat::getLookupName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
