package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.bukkit.Bukkit;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBServer;

import javax.annotation.Nullable;
import java.util.List;

public final class ServerDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    @Nullable
    public static DBServer saveOrUpdate(DBServer player) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        DBServer result = null;

        try {
            transaction.begin();
            result = entityManager.merge(player);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            entityManager.close();
        }
        return result;
    }

    public static DBServer findByName(String name) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.find(DBServer.class, name);
        } finally {
            entityManager.close();
        }
    }

    public static List<DBServer> findAll() {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM servers p", DBServer.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public static void delete(String name) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            DBServer server = entityManager.find(DBServer.class, name);
            if (server != null) {
                entityManager.remove(server);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            entityManager.close();
        }
    }
}
