package sk.adr3ez.globalchallenges.core.database;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import org.sqlite.SQLiteDataSource;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.DatabaseManager;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class CustomPersistenceUnitInfo implements jakarta.persistence.spi.PersistenceUnitInfo {

    private final String puName;
    private final DatabaseManager databaseManager;

    public CustomPersistenceUnitInfo(String puName, DatabaseManager databaseManager) {
        this.puName = puName;
        this.databaseManager = databaseManager;
    }

    @Override
    public String getPersistenceUnitName() {
        return puName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        switch (databaseManager.getStorageMethod()) {
            case MYSQL -> {
                HikariDataSource dataSource = new HikariDataSource();

                String hostname = "localhost";
                String database = "globalchallenges";

                dataSource.setJdbcUrl("jdbc:mysql://" + hostname + "/" + database);
                dataSource.setUsername("root");
                dataSource.setPassword("");
                dataSource.setMinimumIdle(1);
                dataSource.setMaximumPoolSize(20);
                dataSource.setConnectionTimeout(2000);
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setPassword("");

                return dataSource;
            }
            default -> {
                File dbFile = new File(GlobalChallengesProvider.get().getDataDirectory() + "/database.db");
                SQLiteDataSource dataSource = new SQLiteDataSource();
                dataSource.setDatabaseName(puName);
                dataSource.setUrl("jdbc:sqlite:" + GlobalChallengesProvider.get().getDataDirectory() + "/database.db");
                return dataSource;
            }
        }
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of("sk.adr3ez.globalchallenges.api.database.entity.DBPlayer",
                "sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData",
                "sk.adr3ez.globalchallenges.api.database.entity.DBGame",
                "sk.adr3ez.globalchallenges.api.database.entity.DBServer");
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    @Override
    public List<String> getMappingFileNames() {
        return null;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return null;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }


    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return null;
    }

    @Override
    public ValidationMode getValidationMode() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {

    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }

}
