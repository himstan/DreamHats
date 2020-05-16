package me.stan.hatmanager.Commands.SubCommands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stan.hatmanager.Commands.SubCommand;
import me.stan.hatmanager.Configs.SubConfigs.DefaultConfig;
import me.stan.hatmanager.Handlers.HatHandler;
import me.stan.hatmanager.Handlers.HatPlayerManager;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.Hat;
import me.stan.hatmanager.Model.HatPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AddHatCommand extends SubCommand {

    private HatHandler hatHandler;
    private HatPlayerManager hatPlayerManager;

    public AddHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        super(plugin, commandName, commandDescription, commandUsage, permission, commandAliases);
        init();
    }

    public AddHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        super(plugin, commandName, commandDescription, commandUsage, permission);
        init();
    }

    public AddHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
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
        if (args.length <= 0) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getEnterUsername()));
            return true;
        }
        if (args.length == 1) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getEnterHatNameOrID()));
            return true;
        }
        String hatID = args[1];
        boolean success = false;
        boolean online = false;
        String addToName = args[0];
        Player addTo = getPlugin().getServer().getPlayer(addToName);
        Hat hat = hatHandler.getHat(hatID);
        if (hat == null) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(addTo,defaultConfig.getHatNotFound().replace("%hat_name%",hatID)));
            return true;
        }
        if (addTo != null) {
            HatPlayer hatPlayer = hatPlayerManager.getHatPlayer(addTo);
            if (hatPlayer.hasHat(hat)) {
                player.sendRawMessage(PlaceholderAPI.setPlaceholders(addTo,defaultConfig.getHatAlreadyHave().replace("%hat_name%",hat.getHatName())));
                return true;
            }
            success = hatHandler.giveHat(addTo, hat,true, true);
            online = true;
        } else {
            hatHandler.giveHatToOffline(addToName, hat);
            success = true;
        }
        if (success) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(addTo,defaultConfig.getHatAdd().replace("%hat_name%",hat.getHatName())));
            if (online) {
                addTo.sendRawMessage(PlaceholderAPI.setPlaceholders(addTo,defaultConfig.getHatReceive().replace("%hat_name%",hat.getHatName())));
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
