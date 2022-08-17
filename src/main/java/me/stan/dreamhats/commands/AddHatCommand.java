package me.stan.dreamhats.commands;

import hu.stan.dreamcore.commands.SubCommand;
import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatPlayer;
import me.stan.dreamhats.module.PlayerModule;
import me.stan.dreamhats.service.HatService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command for giving hats to players.
 */
public class AddHatCommand extends SubCommand {

  private HatService hatService;
  private PlayerModule playerModule;

  public AddHatCommand(final DreamHats plugin, final String commandName, final String commandDescription, final String commandUsage, final Permission permission) {
    super(plugin, commandName, commandDescription, commandUsage, permission);
    init();
  }

  @Override
  public boolean execute(final Player player, final String commandName, final String label, final String[] args) {
    if (player == null) {
      return true;
    }
    if (args.length <= 0) {
      player.sendRawMessage(TranslationService.translate("error.enter_username", player));
      return true;
    }
    if (args.length == 1) {
      player.sendRawMessage(TranslationService.translate("error.enter_hat_name_or_id", player));
      return true;
    }
    final String hatId = args[1];
    final boolean success;
    boolean online = false;
    final String addToName = args[0];
    final Player addTo = Bukkit.getServer().getPlayer(addToName);
    final Hat hat = hatService.getHat(hatId);
    if (hat == null) {
      player.sendRawMessage(
          TranslationService.translate("error.hat_not_found", player)
              .replace("%hat_name%", hatId));
      return true;
    }
    if (addTo != null) {
      final HatPlayer hatPlayer = playerModule.getHatPlayer(addTo);
      if (hatPlayer.hasHat(hat)) {
        player.sendRawMessage(
            TranslationService.translate("error.hat_already_have", addTo)
                .replace("%hat_name%", hatId));
        return true;
      }
      success = hatService.giveHat(addTo, hat, true, true);
      online = true;
    } else {
      hatService.giveHatToOffline(addToName, hat);
      success = true;
    }
    if (success) {
      player.sendRawMessage(
          TranslationService.translate("add.hat_add", addTo)
              .replace("%hat_name%", hatId));
      if (online) {
        addTo.sendRawMessage(
            TranslationService.translate("add.hat_receive", player)
                .replace("%hat_name%", hatId));
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
