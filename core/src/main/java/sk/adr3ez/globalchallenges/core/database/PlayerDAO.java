package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.bukkit.Bukkit;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayer;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public final class PlayerDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    @Nullable
    public static DBPlayer saveOrUpdate(DBPlayer player) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        DBPlayer result = null;

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

    public static void addData(DBPlayerData playerData) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(playerData);
            entityManager.flush();
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

    public static DBPlayer findByUuid(final UUID uuid) {
        return findByUuid(uuid.toString());
    }

    public static DBPlayer findByUuid(final String uuid) {
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
            return entityManager.createQuery("SELECT p FROM players p", DBPlayer.class).getResultList();
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
