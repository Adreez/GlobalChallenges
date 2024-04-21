package sk.adr3ez.globalchallenges.core.database;

import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class DatabaseManager {

    private final EntityManagerFactory emf = buildEntityManagerFactory();


    private EntityManagerFactory buildEntityManagerFactory() {

        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        GlobalChallenges plugin = GlobalChallengesProvider.get();
        String host = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_HOSTNAME.getRoute());
        String database = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_DATABASE.getRoute());
        String port = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_PORT.getRoute());
        String username = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_USERNAME.getRoute());
        String password = plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_PASSWORD.getRoute());

        getLogger().log(Level.WARNING, "Host: " + host);
        getLogger().log(Level.WARNING, "Database: " + database);
        getLogger().log(Level.WARNING, "Port: " + port);
        getLogger().log(Level.WARNING, "Username: " + username);
        getLogger().log(Level.WARNING, "Password: " + password);

        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/" + database);
        properties.put("javax.persistence.jdbc.user", username);
        properties.put("javax.persistence.jdbc.password", password);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }
        properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        return Persistence.createEntityManagerFactory("PlayerPU", properties);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void close() {
        emf.close();
    }
}
