package sk.adr3ez.globalchallenges.core.database;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    void setup();
    @Nullable
    Connection getConnection() throws SQLException;

    void close();

}
