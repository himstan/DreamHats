package me.stan.dreamhats.LuckPerms;

import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.HatGroup;
import me.stan.dreamhats.service.HatService;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Events {
  private DreamHats plugin;
  private HatService hatService;

  public Events(final DreamHats plugin, final LuckPerms api) {
    this.plugin = plugin;
    this.hatService = plugin.getHatService();
    final EventBus eventBus = api.getEventBus();
    eventBus.subscribe(UserPromoteEvent.class, this::onUserPromote);
  }

  private void onUserPromote(final UserPromoteEvent event) {
    Bukkit.getScheduler().runTask(plugin, () -> {
      final Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
      if (player != null) {
        try {
          player.sendRawMessage(event.getGroupTo().get());
          final HatGroup hatGroup = hatService.getHatGroup(event.getGroupTo().get());
          hatService.giveHatGroup(player, hatGroup);
          player.sendRawMessage(
              TranslationService.translate("hatgroup.add_message", player).replace("%hat_group%", hatGroup.getHatGroupName())
          );
        } catch (final NullPointerException ignored) {
        }
        ;
      }
    });
  }
}
