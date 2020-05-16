package me.stan.hatmanager.DAO;

import me.stan.hatmanager.Configs.SubConfigs.DataBaseConfig;
import me.stan.hatmanager.HatManager;
import me.stan.hatmanager.Model.Hat;
import me.stan.hatmanager.Model.HatPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;

public class IHatDAO implements HatDAO{

    private HatManager plugin;
    private DataBaseConfig dataBaseConfig;
    private Connection connection;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public IHatDAO(HatManager plugin) {
        try {
            synchronized (this) {
                this.plugin = plugin;
                dataBaseConfig = plugin.getConfigManager().getDataBaseConfig();

                host = dataBaseConfig.getDatabaseHost();
                port = dataBaseConfig.getDatabasePort();
                database = dataBaseConfig.getDatabaseName();
                username = dataBaseConfig.getDatabaseUserName();
                password = dataBaseConfig.getDatabasePassword();

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                createTables();
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully connected to the database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + plugin.getName() +  "] Couldn't connect to database! Disabling plugin...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    private final String CREATE_PLAYERHATS_TABLE = "create table if not exists playerhats(" +
            "   playerLookupName varchar(255) not null," +
            "   hatID int not null," +
            "   CONSTRAINT player_hat UNIQUE (playerLookupName, hatID)," +
            "   INDEX PlayerHats_index (playerLookupName));";

    private void createTables() {
        try {
            getConnection().createStatement().execute(CREATE_PLAYERHATS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
        }
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean savePlayerHats(HatPlayer player) {
        if (player.getHats().isEmpty()) {
            return false;
        }
        try {
//            PreparedStatement deleteStatement = createRemoveQuery(player);
            PreparedStatement saveStatement = createSaveQuery(player);
//            deleteStatement.executeUpdate();
            saveStatement.executeBatch();
        } catch (SQLException ignored) {
        }
        return false;
    }

    @Override
    public boolean savePlayerHat(String playerName, Hat hat) {
        if (hat == null) {
            return false;
        }
        try {
            PreparedStatement saveStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName,hatID) VALUES(?,?)");
            saveStatement.setString(1,playerName.toLowerCase());
            saveStatement.setInt(2,hat.getHatID());
            return saveStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public HatPlayer loadPlayerHats(Player player) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM playerhats WHERE playerLookupName = ?");
            preparedStatement.setString(1,player.getName().toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            HatPlayer hatPlayer = new HatPlayer(player.getUniqueId(),player.getName());
            while (resultSet.next()) {
                int hatID = resultSet.getInt("hatID");
                Hat hat = plugin.getHatHandler().getHat(hatID);
                if (hat != null) {
                    hatPlayer.addHat(hat,false, false);
                }
            }
            return hatPlayer;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean removeHatFromPlayer(String playerName, Hat hat) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM playerhats WHERE playerLookupName = ? AND hatID = ?");
            preparedStatement.setString(1,playerName.toLowerCase());
            preparedStatement.setInt(2,hat.getHatID());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean giveHatToOffline(String playerName, Hat hat) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName, hatID) VALUES (?,?)");
            preparedStatement.setString(1,playerName.toLowerCase());
            preparedStatement.setInt(2,hat.getHatID());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private PreparedStatement createSaveQuery(HatPlayer player) throws SQLException {
        String lookupName = player.getPlayerName().toLowerCase();
        PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName,hatID) VALUES(?,?)");
        for (Hat hat : player.getHats()) {
            if (!hat.isFlaggedForRemoval()) {
                preparedStatement.setString(1, lookupName);
                preparedStatement.setInt(2, hat.getHatID());
                preparedStatement.addBatch();
            }
        }
        return preparedStatement;
    }

    private PreparedStatement createRemoveQuery(HatPlayer player) throws SQLException {
        String lookupName = player.getPlayerName().toLowerCase();
        PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM playerhats WHERE playerLookupName = ?");
        preparedStatement.setString(1,lookupName);
        return preparedStatement;
    }
}
