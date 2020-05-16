package me.stan.hatmanager.Configs.SubConfigs;

import me.stan.hatmanager.HatManager;

public class DataBaseConfig extends SubConfig{

    public DataBaseConfig(HatManager plugin, String configName) {
        super(plugin, configName);
    }

    public String getDatabaseHost() {
        String host = config.getString("hostname");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }
        return host;
    }

    public int getDatabasePort() {
        int port = config.getInt("port");
        if (port == 0) {
            port = 3306;
        }
        return port;
    }

    public String getDatabaseName() {
        String name = config.getString("databasename");
        if (name == null) {
            name = "";
        }
        return name;
    }

    public String getDatabaseUserName() {
        String userName = config.getString("username");
        if (userName == null || userName.isEmpty()) {
            userName = "root";
        }
        return userName;
    }

    public String getDatabasePassword() {
        String password = config.getString("password");
        if (password == null || password.isEmpty()) {
            password = "";
        }
        return password;
    }

}
