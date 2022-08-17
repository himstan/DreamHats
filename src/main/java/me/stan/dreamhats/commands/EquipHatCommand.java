package me.stan.dreamhats.commands;

import hu.stan.dreamcore.commands.SubCommand;
import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.module.PlayerModule;
import me.stan.dreamhats.service.HatService;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EquipHatCommand extends SubCommand {

  private HatService hatService;
  private PlayerModule playerModule;

  public EquipHatCommand(final DreamHats plugin, final String commandName, final String commandDescription, final String commandUsage, final Permission permission) {
    super(plugin, commandName, commandDescription, commandUsage, permission);
    init();
  }

  @Override
  public boolean execute(final Player player, final String commandName, final String label, final String[] args) {
    if (player == null || playerModule.getHatPlayer(player) == null) {
      return true;
    }
    if (args.length == 0) {
      player.sendRawMessage(TranslationService.translate("error.enter_hat_name_or_id", player));
      return true;
    }
    final String hatID = args[0];
    final Hat hat = hatService.getHat(hatID);
    if (hat == null) {
      player.sendRawMessage(
          TranslationService.translate("error.hat_not_found", player)
              .replace("%hat_name%", hatID));
      return true;
    }
    if (!playerModule.getHatPlayer(player).hasHat(hat)) {
      player.sendRawMessage(
          TranslationService.translate("error.hat_you_dont_have", player)
              .replace("%hat_name%", hat.getHatName()));
      return true;
    }
    hatService.equipHat(player, hat, false);

    player.sendRawMessage(
        TranslationService.translate("equip.hat_equip", player)
            .replace("%hat_name%", hat.getHatName()));
    return true;
  }

  @Override
  public List<String> tabComplete(final Player player, final String label, final String[] args) {
    if (args.length == 1) {
      return playerModule.getHatPlayer(player).getHats().stream().filter(hat -> !hat.isFlaggedForRemoval()).map(Hat::getLookupName).collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private void init() {
    hatService = DreamHats.getInstance().getHatService();
    playerModule = DreamHats.getInstance().getModuleManager().getModule(PlayerModule.class);
  }
}
