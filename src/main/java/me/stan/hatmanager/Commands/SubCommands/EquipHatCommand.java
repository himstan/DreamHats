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

public class EquipHatCommand extends SubCommand {

    private HatHandler hatHandler;
    private HatPlayerManager hatPlayerManager;

    public EquipHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission, List<String> commandAliases) {
        super(plugin, commandName, commandDescription, commandUsage, permission, commandAliases);
        init();
    }

    public EquipHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage, Permission permission) {
        super(plugin, commandName, commandDescription, commandUsage, permission);
        init();
    }

    public EquipHatCommand(HatManager plugin, String commandName, String commandDescription, String commandUsage) {
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
        if (player == null || hatPlayerManager.getHatPlayer(player) == null) {
            return true;
        }
        if (args.length == 0) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getEnterHatNameOrID()));
            return true;
        }
        String hatID = args[0];
        Hat hat = hatHandler.getHat(hatID);
        if (hat == null) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getHatNotFound().replace("%hat_name%",hatID)));
            return true;
        }
        if (!hatPlayerManager.getHatPlayer(player).hasHat(hat)) {
            player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getHatDontHaveMessage().replace("%hat_name%",hat.getHatName())));
            return true;
        }
        hatHandler.equipHat(player,hat,false);
        player.sendRawMessage(PlaceholderAPI.setPlaceholders(player,defaultConfig.getHatEquip().replace("%hat_name%",hat.getHatName())));
        return true;
    }

    @Override
    public List<String> tabComplete(Player player, String label, String[] args) {
        if (args.length == 1) {
            return hatPlayerManager.getHatPlayer(player).getHats().stream().filter(hat -> !hat.isFlaggedForRemoval()).map(Hat::getLookupName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
