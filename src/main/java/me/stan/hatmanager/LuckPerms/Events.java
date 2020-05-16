package me.stan.hatmanager.LuckPerms;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stan.hatmanager.Handlers.HatHandler;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.HatGroup;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Events {
    private HatManager plugin;
    private LuckPerms api;
    private HatHandler hatHandler;

    public Events(HatManager plugin, LuckPerms api) {
        this.plugin = plugin;
        this.api = api;
        this.hatHandler = plugin.getHatHandler();
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(UserPromoteEvent.class, this::onUserPromote);
    }

    private void onUserPromote(UserPromoteEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
            if (player != null) {
                try {
                    player.sendRawMessage(event.getGroupTo().get());
                    HatGroup hatGroup = hatHandler.getHatGroup(event.getGroupTo().get());
                    hatHandler.giveHatGroup(player, hatGroup);
                    player.sendRawMessage(PlaceholderAPI.setPlaceholders(player, plugin.getConfigManager().getDefaultConfig().getHatGroupAdd().replace("%hat_group_name%", hatGroup.getHatGroupName())));
                } catch (NullPointerException ignored){};
            }
        });
    }
}
