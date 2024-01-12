package sk.adr3ez.globalchallenges.core.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractTable {

    @NotNull
    protected final String table;
    @NotNull
    protected final ConnectionFactory manager;
    @NotNull
    protected final GlobalChallenges plugin;

    public abstract void createTable();

    public AbstractTable(@NotNull ConnectionFactory manager, @NotNull GlobalChallenges plugin, @NotNull String table) {
        this.manager = manager;
        this.table = table;
        this.plugin = plugin;
        createTable();
    }

    public void close(@Nullable Connection connection, @Nullable PreparedStatement preparedStatement, @Nullable ResultSet resultSet) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignored) {
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public void close(@Nullable Connection connection, @Nullable PreparedStatement preparedStatement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ignored) {
            }
        }
    }

}
