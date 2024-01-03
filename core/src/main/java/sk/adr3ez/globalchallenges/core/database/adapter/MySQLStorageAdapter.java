package sk.adr3ez.globalchallenges.core.database.adapter;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.Storage;
import sk.adr3ez.globalchallenges.core.database.mysql.AbstractTable;
import sk.adr3ez.globalchallenges.core.database.mysql.MySQLConnectionFactoryAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLStorageAdapter extends AbstractTable implements Storage {
    public MySQLStorageAdapter(@NotNull MySQLConnectionFactoryAdapter manager, @NotNull GlobalChallenges plugin, @NotNull String table) {
        super(manager, plugin, table);
    }

    @Override
    public void createTable() {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
                    "games_played INT DEFAULT 0," +
                    "games_won INT DEFAULT 0");
            preparedStatement.execute();
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
        } finally {
            close(connection, preparedStatement, null);
        }
    }

    @Override
    public boolean createUser(@NotNull UUID uuid) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("INSERT INTO " + table + " (uuid) VALUES (?) ");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
        } finally {
            close(connection, preparedStatement, null);
        }
        return false;
    }

    @Override
    public boolean exists(@NotNull UUID uuid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
            return false;
        } finally {
            close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public boolean deleteUser(@NotNull UUID uuid) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("DELETE FROM " + table + " WHERE uuid=? ");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
        } finally {
            close(connection, preparedStatement, null);
        }
        return false;
    }

    @Override
    public boolean addJoin(@NotNull UUID uuid) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            assert connection != null;

            int gamesJoined = getJoins(uuid);
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET games_played=? WHERE uuid=? ");
            preparedStatement.setInt(1, gamesJoined + 1);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
        } finally {
            close(connection, preparedStatement, null);
        }
        return false;
    }

    @Override
    public boolean addWin(@NotNull UUID uuid) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = manager.getConnection();
            assert connection != null;

            int gamesWinned = getJoins(uuid);
            preparedStatement = connection.prepareStatement("UPDATE " + table + " SET games_won=? WHERE uuid=? ");
            preparedStatement.setInt(1, gamesWinned + 1);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            plugin.getPluginLogger().severe(e.getMessage());
        } finally {
            close(connection, preparedStatement, null);
        }
        return false;
    }

    @Override
    public int getWins(@NotNull UUID uuid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("games_won");
            }

        } catch (SQLException ignored) {
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return 0;
    }

    @Override
    public int getJoins(@NotNull UUID uuid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = manager.getConnection();
            assert connection != null;
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE uuid=?");
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("games_played");
            }

        } catch (SQLException ignored) {
        } finally {
            close(connection, preparedStatement, resultSet);
        }
        return 0;
    }
}
