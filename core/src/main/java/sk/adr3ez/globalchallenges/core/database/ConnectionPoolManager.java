package sk.adr3ez.globalchallenges.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {

    @Nullable
    @Getter
    private HikariDataSource dataSource;

    @NotNull
    @Setter
    @Accessors(chain = true)
    private String hostname = "localhost";
    @NotNull
    @Setter
    @Accessors(chain = true)
    private String database = "globalchallenges";
    @NotNull
    @Setter
    @Accessors(chain = true)
    private String username = "root";
    @NotNull
    @Setter
    @Accessors(chain = true)
    private String password = "";

    @Setter
    @Accessors(chain = true)
    private int minimumConnections = 1;
    @Setter
    @Accessors(chain = true)
    private int maximumConnections = 20;
    @Setter
    @Accessors(chain = true)
    private long connectionTimeout = 2000;

    /**
     * This method will create new HikariConfig with all properties and then
     * new HikariDataSource will be created.
     */
    public ConnectionPoolManager() {
    }

    public void setup() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" + hostname + "/" + database);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumConnections);
        config.setMaximumPoolSize(maximumConnections);
        config.setConnectionTimeout(connectionTimeout);

        dataSource = new HikariDataSource(config);
    }

    @Nullable
    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed())
            setup();
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }
}
