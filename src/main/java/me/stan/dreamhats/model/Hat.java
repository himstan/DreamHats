package me.stan.dreamhats.model;

import org.bukkit.ChatColor;

import java.util.Objects;

public class Hat {

  private int hatID;
  private String hatName;
  private String lookupName;
  private int hatModelData;
  private boolean flaggedForRemoval = false;

  public Hat(final int hatID, final String lookupName, final String hatName, final int hatModelData) {
    this.hatID = hatID;
    this.lookupName = lookupName;
    this.hatName = hatName;
    this.hatModelData = hatModelData;
  }

  public Hat(final Hat hat) {
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
    return ChatColor.translateAlternateColorCodes('&', hatName);
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
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Hat hat = (Hat) o;
    return hatID == hat.hatID &&
        hatModelData == hat.hatModelData &&
        Objects.equals(hatName, hat.hatName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hatID, hatName, hatModelData);
  }
}
