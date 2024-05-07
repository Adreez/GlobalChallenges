package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public final class DatabaseManagerImp implements DatabaseManager {

    private final EntityManagerFactory entityManagerFactory;

    private final GlobalChallenges globalChallenges;

    public DatabaseManagerImp(GlobalChallenges globalChallenges) {
        this.globalChallenges = globalChallenges;

        Map<String, String> props = new HashMap<>();
        props.put("hibernate.show_sql", "false"); //DEBUG
        props.put("hibernate.hbm2ddl.auto", "update"); // create, none, update
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // create, none, update

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        this.entityManagerFactory = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo("GlobalChallenges"), props);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }
}
