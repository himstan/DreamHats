package me.stan.hatmanager.Controller;

import me.stan.hatmanager.DAO.HatDAO;
import me.stan.hatmanager.HatManager;

public class HatController {

    private HatDAO hatDAO;
    private HatManager plugin;

    public HatController(HatManager plugin) {
        this.plugin = plugin;
        hatDAO = plugin.getHatDAO();
    }
}
