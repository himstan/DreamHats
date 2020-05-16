package me.stan.hatmanager.Model;

import org.bukkit.ChatColor;

import java.util.Objects;

public class Hat {

    private int hatID;
    private String hatName;
    private String lookupName;
    private int hatModelData;
    private boolean flaggedForRemoval = false;

    public Hat(int hatID, String lookupName, String hatName, int hatModelData) {
        this.hatID = hatID;
        this.lookupName = lookupName;
        this.hatName = hatName;
        this.hatModelData = hatModelData;
    }

    public Hat(Hat hat) {
        this.hatID = hat.getHatID();
        this.lookupName = hat.getLookupName();
        this.hatName = hat.hatName;
        this.hatModelData = hat.getHatModelData();
        this.flaggedForRemoval = false;
    }

    public int getHatID() {
        return this.hatID;
    }

    public String getLookupName() {
        return lookupName;
    }

    public String getHatName() {
        return ChatColor.translateAlternateColorCodes('&',hatName);
    }

    public int getHatModelData() {
        return hatModelData;
    }

    public boolean isFlaggedForRemoval() {
        return flaggedForRemoval;
    }

    public void flagForRemoval() {
        flaggedForRemoval = true;
    }

    public void unflagForRemoval() {
        flaggedForRemoval = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hat hat = (Hat) o;
        return hatID == hat.hatID &&
                hatModelData == hat.hatModelData &&
                Objects.equals(hatName, hat.hatName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hatID, hatName, hatModelData);
    }
}
