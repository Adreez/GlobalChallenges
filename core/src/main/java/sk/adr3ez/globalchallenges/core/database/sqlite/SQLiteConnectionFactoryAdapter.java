package sk.adr3ez.globalchallenges.core.database.sqlite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.core.database.ConnectionFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionFactoryAdapter implements ConnectionFactory {

    @NotNull
    private final GlobalChallenges plugin;

    @Nullable
    private Connection connection;

    public SQLiteConnectionFactoryAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        File dataFolder = new File(plugin.getDataDirectory(), "globalchallenges.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getPluginLogger().severe("File write error: globalchallenges.db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
        } catch (SQLException ex) {
            plugin.getPluginLogger().severe("SQLite exception on initialize" + ex);
        } catch (ClassNotFoundException ex) {
            plugin.getPluginLogger().severe("You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
    }

    @Nullable
    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws RuntimeException {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
