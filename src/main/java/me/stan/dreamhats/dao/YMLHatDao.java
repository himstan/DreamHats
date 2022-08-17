package me.stan.dreamhats.dao;

import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatPlayer;
import me.stan.dreamhats.repository.HatYmlRepository;
import org.bukkit.entity.Player;

public class YMLHatDao implements HatDAO {

  private final HatYmlRepository hatYmlRepository;

  public YMLHatDao(final DreamHats dreamHats) {
    hatYmlRepository = new HatYmlRepository(dreamHats);
    dreamHats.getLogger().info("Database initialized: YML");
  }

  @Override
  public boolean savePlayerHats(final HatPlayer player) {
    hatYmlRepository.savePlayer(player.getPlayerName(),
        player.getHats().stream().map(Hat::getHatID).toList());
    return true;
  }

  @Override
  public boolean savePlayerHat(final String playerName, final Hat hat) {
    hatYmlRepository.addHat(playerName, hat.getHatID());
    return true;
  }

  @Override
  public HatPlayer loadPlayerHats(final Player player) {
    final var hatHandler = DreamHats.getInstance().getHatService();
    final var hatIds = hatYmlRepository.getHatIds(player.getName());
    final var hatPlayer = new HatPlayer(player.getUniqueId(), player.getName());
    hatIds.stream().map(hatHandler::getHat).forEach(hat -> hatPlayer.addHat(hat, false, false));
    return hatPlayer;
  }

  @Override
  public boolean removeHatFromPlayer(final String playerName, final Hat hat) {
    hatYmlRepository.removeHat(playerName, hat.getHatID());
    return true;
  }

  @Override
  public boolean giveHatToOffline(final String playerName, final Hat hat) {
    return savePlayerHat(playerName, hat);
  }
}
