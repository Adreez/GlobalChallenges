package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.bukkit.Bukkit;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;

import javax.annotation.Nullable;
import java.util.List;

public final class PlayerDataDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    @Nullable
    public static DBPlayerData saveOrUpdate(DBPlayerData player) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        DBPlayerData result = null;

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

    public static DBPlayerData findByUuid(String uuid) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.find(DBPlayerData.class, uuid);
        } finally {
            entityManager.close();
        }
    }

    public static List<DBPlayerData> findAll() {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM player_data p", DBPlayerData.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public static void delete(String uuid) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            DBPlayerData player = entityManager.find(DBPlayerData.class, uuid);
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
