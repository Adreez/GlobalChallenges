package sk.adr3ez.globalchallenges.core.database.mysql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.core.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractTable {

    @NotNull
    protected String table;
    @NotNull
    protected ConnectionFactory manager;

    public abstract void createTable();

    public AbstractTable(@NotNull ConnectionFactory manager, @NotNull String table) {
        this.manager = manager;
        this.table = table;
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
