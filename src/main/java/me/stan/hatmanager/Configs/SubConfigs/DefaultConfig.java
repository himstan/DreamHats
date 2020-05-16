package me.stan.hatmanager.Configs.SubConfigs;

import me.stan.hatmanager.HatManager;
import org.bukkit.ChatColor;

public class DefaultConfig extends SubConfig{

    public DefaultConfig(HatManager plugin, String configName) {
        super(plugin, configName);
    }


    public String getHatGroupAdd() {
        String msg = config.getString("hatgroup.add_message");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatEquipPerm() {
        String msg = config.getString("permissions.hat_equip");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatAddPerm() {
        String msg = config.getString("permissions.hat_add");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatRemovePerm() {
        String msg = config.getString("permissions.hat_remove");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getNoEquipPermError() {
        String msg = config.getString("permission_messages.equip_error");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getAddSoundEffect() {
        String msg = config.getString("add.soundeffect");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public boolean isAddSoundEffectGlobal() {
        return config.getBoolean("add.globalsound");
    }

    public String getRemoveSoundEffect() {
        String msg = config.getString("remove.soundeffect");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public boolean isRemoveSoundEffectGlobal() {
        return config.getBoolean("remove.globalsound");
    }

    public String getEquipSoundEffect() {
        String msg = config.getString("equip.soundeffect");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public boolean isEquipSoundEffectGlobal() {
        return config.getBoolean("equip.globalsound");
    }

    public String getDequipSoundEffect() {
        String msg = config.getString("dequip.soundeffect");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public boolean isDequipSoundEffectGlobal() {
        return config.getBoolean("dequip.globalsound");
    }

    public String getHatLoreInv() {
        String msg = config.getString("hat.lore_inventory");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatLoreGUI() {
        String msg = config.getString("hat.lore_recipegui");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatAlreadyEquippedMessage() {
        String msg = config.getString("error.hat_already_equipped");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatDontHaveMessage() {
        String msg = config.getString("error.hat_you_dont_have");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatAlreadyHave() {
        String msg = config.getString("error.hat_already_have");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatPlayerDoesntHave() {
        String msg = config.getString("error.hat_player_doesnt_have");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatNotFound() {
        String msg = config.getString("error.hat_not_found");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getEnterUsername() {
        String msg = config.getString("error.enter_username");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getEnterHatNameOrID() {
        String msg = config.getString("error.enter_hat_name_or_id");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatAdd() {
        String msg = config.getString("add.hat_add");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatReceive() {
        String msg = config.getString("add.hat_receive");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatRemove() {
        String msg = config.getString("remove.hat_remove");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatRemoved() {
        String msg = config.getString("remove.hat_removed");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatDequippedWithRemove() {
        String msg = config.getString("remove.hat_dequipped_with_remove");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatEquip() {
        String msg = config.getString("equip.hat_equip");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String getHatDequip() {
        String msg = config.getString("dequip.hat_dequip");
        if (msg == null) {
            msg = "";
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

}
