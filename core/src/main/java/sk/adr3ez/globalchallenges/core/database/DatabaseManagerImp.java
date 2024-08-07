package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.hibernate.jpa.HibernatePersistenceProvider;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.DatabaseManager;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.api.util.StorageMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public final class DatabaseManagerImp implements DatabaseManager {

    private final EntityManagerFactory entityManagerFactory;

    private final GlobalChallenges globalChallenges;

    private StorageMethod storageMethod = StorageMethod.SQLITE;

    public DatabaseManagerImp(GlobalChallenges globalChallenges) {
        this.globalChallenges = globalChallenges;

        try {
            StorageMethod sm = StorageMethod.valueOf(globalChallenges.getConfiguration().getString(ConfigRoutes.STORAGE_METHOD.getRoute()));
            if (sm != null)
                storageMethod = sm;
        } catch (IllegalArgumentException ignored) {
            globalChallenges.getPluginLogger().warn("FAILED to identify storage-method in config.yml! Please check your configuration. Setting default method to SQLITE.");
        }

        this.entityManagerFactory = createEntityManagerFactory();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public StorageMethod getStorageMethod() {
        return storageMethod;
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }

    private EntityManagerFactory createEntityManagerFactory() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.show_sql", "false"); //DEBUG
        props.put("hibernate.hbm2ddl.auto", "update"); // create, none, update
        props.put("hibernate.dialect", storageMethod.getDriverClassName());

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        EntityManagerFactory managerFactory;
        try {
            managerFactory = new HibernatePersistenceProvider()
                    .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo("GlobalChallenges", this), props);
        } catch (PersistenceException ignored) {
            return null;
        }
        return managerFactory;
    }
}
