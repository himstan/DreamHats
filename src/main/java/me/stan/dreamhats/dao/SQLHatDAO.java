package me.stan.dreamhats.dao;

import hu.stan.dreamcore.configs.subconfigs.SubConfig;
import me.stan.dreamhats.DreamHats;
import me.stan.dreamhats.model.Hat;
import me.stan.dreamhats.model.HatPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;

public class SQLHatDAO implements HatDAO {

  private final String CREATE_PLAYERHATS_TABLE = "create table if not exists playerhats(" +
      "   playerLookupName varchar(255) not null," +
      "   hatID int not null," +
      "   CONSTRAINT player_hat UNIQUE (playerLookupName, hatID)," +
      "   INDEX PlayerHats_index (playerLookupName));";
  private DreamHats plugin;
  private SubConfig config;
  private Connection connection;
  private String host;
  private int port;
  private String database;
  private String username;
  private String password;

  public SQLHatDAO(final DreamHats plugin) {
    try {
      synchronized (this) {
        this.plugin = plugin;
        config = plugin.getConfigManager().getSubConfig("dbconfig");

        host = config.getConfig().getString("hostname");
        port = config.getConfig().getInt("port");
        database = config.getConfig().getString("databasename");
        username = config.getConfig().getString("username");
        password = config.getConfig().getString("password");

        Class.forName("com.mysql.jdbc.Driver");
        setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
        createTables();
        plugin.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully connected to the database!");
      }
    } catch (final ClassNotFoundException | SQLException e) {
      plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[" + plugin.getName() + "] Couldn't connect to database! Disabling plugin...");
      plugin.getServer().getPluginManager().disablePlugin(plugin);
    }
  }

  @Override
  public boolean savePlayerHats(final HatPlayer player) {
    if (player.getHats().isEmpty()) {
      return false;
    }
    try {
//            PreparedStatement deleteStatement = createRemoveQuery(player);
      final PreparedStatement saveStatement = createSaveQuery(player);
//            deleteStatement.executeUpdate();
      saveStatement.executeBatch();
    } catch (final SQLException ignored) {
    }
    return false;
  }

  @Override
  public boolean savePlayerHat(final String playerName, final Hat hat) {
    if (hat == null) {
      return false;
    }
    try {
      final PreparedStatement saveStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName,hatID) VALUES(?,?)");
      saveStatement.setString(1, playerName.toLowerCase());
      saveStatement.setInt(2, hat.getHatID());
      return saveStatement.execute();
    } catch (final SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  @Override
  public HatPlayer loadPlayerHats(final Player player) {
    try {
      final PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM playerhats WHERE playerLookupName = ?");
      preparedStatement.setString(1, player.getName().toLowerCase());
      final ResultSet resultSet = preparedStatement.executeQuery();
      final HatPlayer hatPlayer = new HatPlayer(player.getUniqueId(), player.getName());
      while (resultSet.next()) {
        final int hatID = resultSet.getInt("hatID");
        final Hat hat = plugin.getHatService().getHat(hatID);
        if (hat != null) {
          hatPlayer.addHat(hat, false, false);
        }
      }
      return hatPlayer;
    } catch (final SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean removeHatFromPlayer(final String playerName, final Hat hat) {
    try {
      final PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM playerhats WHERE playerLookupName = ? AND hatID = ?");
      preparedStatement.setString(1, playerName.toLowerCase());
      preparedStatement.setInt(2, hat.getHatID());
      preparedStatement.executeUpdate();
      return true;
    } catch (final SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean giveHatToOffline(final String playerName, final Hat hat) {
    try {
      final PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName, hatID) VALUES (?,?)");
      preparedStatement.setString(1, playerName.toLowerCase());
      preparedStatement.setInt(2, hat.getHatID());
      preparedStatement.executeUpdate();
      return true;
    } catch (final SQLException throwables) {
      throwables.printStackTrace();
    }
    return false;
  }

  private void createTables() {
    try {
      getConnection().createStatement().execute(CREATE_PLAYERHATS_TABLE);
    } catch (final SQLException e) {
      e.printStackTrace();
    }
  }

  private Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
    }
    return connection;
  }

  private void setConnection(final Connection connection) {
    this.connection = connection;
  }

  private PreparedStatement createSaveQuery(final HatPlayer player) throws SQLException {
    final String lookupName = player.getPlayerName().toLowerCase();
    final PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO playerhats(playerLookupName,hatID) VALUES(?,?)");
    for (final Hat hat : player.getHats()) {
      if (!hat.isFlaggedForRemoval()) {
        preparedStatement.setString(1, lookupName);
        preparedStatement.setInt(2, hat.getHatID());
        preparedStatement.addBatch();
      }
    }
    return preparedStatement;
  }

  private PreparedStatement createRemoveQuery(final HatPlayer player) throws SQLException {
    final String lookupName = player.getPlayerName().toLowerCase();
    final PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM playerhats WHERE playerLookupName = ?");
    preparedStatement.setString(1, lookupName);
    return preparedStatement;
  }
}
