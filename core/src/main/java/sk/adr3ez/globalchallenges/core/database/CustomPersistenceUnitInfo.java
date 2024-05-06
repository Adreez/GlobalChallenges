package sk.adr3ez.globalchallenges.core.database;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

import javax.sql.DataSource;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class CustomPersistenceUnitInfo implements jakarta.persistence.spi.PersistenceUnitInfo {

    private final String puName;

    public CustomPersistenceUnitInfo(String puName) {
        this.puName = puName;
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

    @Override
    public List<String> getManagedClassNames() {
        return List.of("sk.adr3ez.globalchallenges.core.database.entity.DBPlayer", "sk.adr3ez.globalchallenges.core.database.entity.DBPlayerData",
                "sk.adr3ez.globalchallenges.core.database.entity.DBGame",
                "sk.adr3ez.globalchallenges.core.database.entity.DBServer");
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
