package sk.adr3ez.globalchallenges.core.database.sqlite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.ConnectionFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionFactoryAdapter implements ConnectionFactory {

    @NotNull
    private final GlobalChallenges plugin;

    @Nullable
    private File dataFolder = null;


    public SQLiteConnectionFactoryAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
        setup();
    }

    @Override
    public void setup() {
        dataFolder = new File(plugin.getDataDirectory(), "globalchallenges.db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getPluginLogger().warn("File write error: globalchallenges.db");
            }
        }
    }

    @Nullable
    @Override
    public Connection getConnection() {
        try {
            if (dataFolder != null) {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection("jdbc:sqlite:file:" + dataFolder.getAbsolutePath());
            }
            System.out.println("jdbc:sqlite:file:" + dataFolder);
        } catch (SQLException ex) {
            plugin.getPluginLogger().warn("SQLite exception on initialize" + ex);
        } catch (ClassNotFoundException ex) {
            plugin.getPluginLogger().warn("You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    @Override
    public void close() throws RuntimeException {
        /*try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
