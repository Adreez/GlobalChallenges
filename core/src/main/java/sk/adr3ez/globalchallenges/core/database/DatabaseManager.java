package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jetbrains.annotations.ApiStatus;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.util.HashMap;
import java.util.Map;

@ApiStatus.Obsolete
public class DatabaseManager {

    private static final EntityManagerFactory emf = setupEntityManagerFactory();


    private static EntityManagerFactory setupEntityManagerFactory() {

        GlobalChallenges plugin = GlobalChallengesProvider.get();

        Thread.currentThread().setContextClassLoader(plugin.getJavaPlugin().getClass().getClassLoader());

        String host = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_HOSTNAME.getRoute());
        String database = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_DATABASE.getRoute());
        String port = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_PORT.getRoute());
        String username = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_USERNAME.getRoute());
        String password = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_PASSWORD.getRoute());

        Map<String, String> properties = new HashMap<>();
        /*properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
        properties.put("javax.persistence.jdbc.user", username);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");*/
        return Persistence.createEntityManagerFactory("PlayerPU", properties);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        emf.close();
    }
}
