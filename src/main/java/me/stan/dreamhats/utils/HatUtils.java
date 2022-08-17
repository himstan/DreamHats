package me.stan.dreamhats.utils;

import hu.stan.dreamcore.translation.TranslationService;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Objects;

public class HatUtils {
  public static ItemStack createHatItem(final Hat hat) {
    return createHatItem(hat, null);
  }

  public static ItemStack createHatItem(final Hat hat, final Player player) {
    final var hatItem = new ItemStack(Material.STICK);
    final var hatMeta = hatItem.getItemMeta();
    final var lore = Objects.nonNull(player)
        ? TranslationService.translate("hat.lore_inventory", player)
        : TranslationService.translate("hat.lore_inventory");
    if (hatMeta != null) {
      hatMeta.setCustomModelData(hat.getHatModelData());
      hatMeta.setDisplayName(hat.getHatName());
      hatMeta.setLore(Collections.singletonList(lore));
      hatItem.setItemMeta(hatMeta);
    }
    return hatItem;
  }

  public static boolean isHat(final ItemStack itemStack) {
    if (itemStack.getType().equals(Material.STICK)) {
      final var hatMeta = itemStack.getItemMeta();
      final var hatIds = DreamHats.getInstance()
          .getHatService().getAllHats().stream().map(Hat::getHatModelData).toList();
      return Objects.nonNull(hatMeta) && hatIds.contains(hatMeta.getCustomModelData());
    }
    return false;
  }

  public static Integer getHatId(final ItemStack itemStack) {
    if (isHat(itemStack)) {
      final var hatMeta = itemStack.getItemMeta();
      return DreamHats.getInstance()
          .getHatService().getAllHats().stream().filter(hat -> hat.getHatModelData() == hatMeta.getCustomModelData())
          .findAny().get().getHatID();
    }
    return null;
  }
}
