package me.stan.hatmanager.Model;

import java.util.Collection;

public class HatGroup {
    private String hatGroupName;
    private String hatGroupPermName;
    private Collection<Hat> hatSet;

    public HatGroup(String hatGroupName, String hatGroupPermName, Collection<Hat> hatSet) {
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
