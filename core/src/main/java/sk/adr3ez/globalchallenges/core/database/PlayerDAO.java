package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.bukkit.Bukkit;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.core.database.entity.DBPlayer;

import java.util.List;

public final class PlayerDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    public static void saveOrUpdate(DBPlayer player) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(player);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Bukkit.getLogger().warning(e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public static DBPlayer findById(String uuid) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.find(DBPlayer.class, uuid);
        } finally {
            entityManager.close();
        }
    }

    public static List<DBPlayer> findAll() {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Player p", DBPlayer.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public static void delete(String uuid) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            DBPlayer player = entityManager.find(DBPlayer.class, uuid);
            if (player != null) {
                entityManager.remove(player);
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
