package me.stan.dreamhats.model;

import java.util.Collection;

public class HatGroup {
  private String hatGroupName;
  private String hatGroupPermName;
  private Collection<Hat> hatSet;

  public HatGroup(final String hatGroupName, final String hatGroupPermName, final Collection<Hat> hatSet) {
    this.hatGroupName = hatGroupName;
    this.hatGroupPermName = hatGroupPermName;
    this.hatSet = hatSet;
  }

  public String getHatGroupName() {
    return hatGroupName;
  }

  public String getHatGroupPermName() {
    return hatGroupPermName;
  }

  public Collection<Hat> getHats() {
    return hatSet;
  }
}
