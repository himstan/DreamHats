package me.stan.dreamhats.commands;

import hu.stan.dreamcore.commands.SubCommand;
import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.module.PlayerModule;
import me.stan.dreamhats.service.HatService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveHatCommand extends SubCommand {

  private HatService hatService;
  private PlayerModule playerModule;

  public RemoveHatCommand(final DreamHats plugin, final String commandName, final String commandDescription, final String commandUsage, final Permission permission) {
    super(plugin, commandName, commandDescription, commandUsage, permission);
    init();
  }

  @Override
  public boolean execute(final Player player, final String commandName, final String label, final String[] args) {
    if (player == null) {
      return true;
    }
    if (args.length == 0) {
      player.sendRawMessage(TranslationService.translate("error.enter_username", player));
      return true;
    }
    if (args.length == 1) {
      player.sendRawMessage(TranslationService.translate("error.enter_hat_name_or_id", player));
      return true;
    }
    if (args.length > 2) {
      player.sendRawMessage("&4Usage: /" + label + " " + getCommandUsage());
      return true;
    }
    final boolean success;
    boolean online = false;
    final String removeFromName = args[0];
    final String hatID = args[1];
    final Player removeFrom = Bukkit.getServer().getPlayer(removeFromName);
    final Hat hat = hatService.getHat(hatID);
    if (hat == null) {
      player.sendRawMessage(
          TranslationService.translate("error.hat_not_found", removeFrom)
              .replace("%hat_name%", hatID));
      return true;
    }
    if (removeFrom != null) {
      if (!playerModule.getHatPlayer(removeFrom).hasHat(hat)) {
        player.sendRawMessage(
            TranslationService.translate("error.hat_player_doesnt_have", removeFrom)
                .replace("%hat_name%", hatID));
        return true;
      }
      success = hatService.removeHat(removeFrom, hat, true);
      online = true;
    } else {
      success = hatService.removeHatFromOffline(removeFromName, hat);
    }
    if (success) {
      player.sendRawMessage(
          TranslationService.translate("remove.hat_remove", removeFrom)
              .replace("%hat_name%", hatID));
      if (online) {
        removeFrom.sendRawMessage(
            TranslationService.translate("remove.hat_removed", removeFrom)
                .replace("%hat_name%", hatID));
      }
    }
    return true;
  }

  @Override
  public List<String> tabComplete(final Player player, final String label, final String[] args) {
    if (args.length == 1) {
      return null;
    } else if (args.length == 2) {
      return hatService.getAllHats().stream().map(Hat::getLookupName).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private void init() {
    hatService = DreamHats.getInstance().getHatService();
    playerModule = DreamHats.getInstance().getModuleManager().getModule(PlayerModule.class);
  }
}
