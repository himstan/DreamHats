package me.stan.dreamhats.repository;

import hu.stan.dreamcore.configs.subconfigs.SubConfig;
import me.stan.dreamhats.DreamHats;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.List;

public class HatYmlRepository {

    private final SubConfig config;

    public HatYmlRepository(final DreamHats plugin) {
        this.config = plugin.getConfigManager().getSubConfig("save");
    }

    public void savePlayer(final String playerName, final List<Integer> hatIds) {
        final var playerId = playerName.toLowerCase();
        final var section = getOrCreateSection(playerId);
        section.set("hats", hatIds);
        config.getConfig().set(playerId, section);
        config.saveConfig();
    }

    public void addHat(final String playerName, final Integer hatId) {
        final var playerId = playerName.toLowerCase();
        final var section = getOrCreateSection(playerId);
        final var hats = section.getIntegerList("hats");
        if (!hats.contains(hatId)) {
            hats.add(hatId);
        }
        section.set("hats", hats);
        config.getConfig().set(playerId, section);
        config.saveConfig();
    }

    public void removeHat(final String playerName, final Integer hatId) {
        final var playerId = playerName.toLowerCase();
        final var section = getOrCreateSection(playerId);
        final var hats = section.getIntegerList("hats");
        hats.remove(hatId);
        section.set("hats", hats);
        config.getConfig().set(playerId, section);
        config.saveConfig();
    }

    public List<Integer> getHatIds(final String playerName) {
        final var playerId = playerName.toLowerCase();
        return config.getConfig().isConfigurationSection(playerId)
            ? config.getConfig().getConfigurationSection(playerId).getIntegerList("hats")
            : Collections.emptyList();
    }

    public void deletePlayer(final String playerName) {
        final var playerId = playerName.toLowerCase();
        config.getConfig().set(playerId, null);
    }

    private ConfigurationSection getOrCreateSection(final String key) {
        return config.getConfig().isConfigurationSection(key)
            ? config.getConfig().getConfigurationSection(key)
            : config.getConfig().createSection(key);
    }
}
