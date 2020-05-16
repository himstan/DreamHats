package me.stan.hatmanager.Utils;

import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.Hat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class HatUtils {
    public static ItemStack createHatItem(Hat hat) {
        ItemStack hatItem = new ItemStack(Material.STICK);
        ItemMeta hatMeta = hatItem.getItemMeta();
        if (hatMeta != null) {
            hatMeta.setCustomModelData(hat.getHatModelData());
            hatMeta.setDisplayName(hat.getHatName());
            hatMeta.setLore(Collections.singletonList(HatManager.getInstance().getConfigManager().getDefaultConfig().getHatLoreInv()));
            hatItem.setItemMeta(hatMeta);
        }
        return hatItem;
    }
}
