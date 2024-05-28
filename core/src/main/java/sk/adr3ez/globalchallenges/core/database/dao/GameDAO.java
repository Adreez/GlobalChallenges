package sk.adr3ez.globalchallenges.core.database.dao;

import jakarta.persistence.*;
import org.hibernate.HibernateException;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBGame;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;

import javax.annotation.Nullable;
import java.util.List;

public final class GameDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    @Nullable
    public static DBGame saveOrUpdate(DBGame game) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        DBGame returnObject = null;
        try {
            transaction.begin();
            returnObject = entityManager.merge(game);
            transaction.commit();
        } catch (PersistenceException ignored) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }
        return returnObject;
    }

    @Nullable
    public static DBGame findById(Long id) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.find(DBGame.class, id);
        } finally {
            entityManager.close();
        }
    }

    public static DBGame getLast() {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();

        try {
            Query query = entityManager.createQuery("SELECT MAX(e.id) FROM DBGame e");
            Long id = (Long) query.getSingleResult();
            return findById(id);
        } finally {
            entityManager.close();
        }

    }

    public static List<DBGame> findAll() {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.createQuery("SELECT g FROM games g", DBGame.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public static List<DBPlayerData> getPlayerData(final Long gameId) {
        return getPlayerData(findById(gameId));
    }

    /**
     * Gets all playerData for specified game
     *
     * @param gameId ID of the game you want to find
     * @return List<DBPlayerData>
     */
    public static List<DBPlayerData> getPlayerData(DBGame dbGame) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            // Correct query to select DBPlayerData entities where the game matches the provided DBGame entity
            TypedQuery<DBPlayerData> query = entityManager.createQuery(
                    "SELECT e FROM DBPlayerData e WHERE e.game = :game", DBPlayerData.class);
            // Set the parameter with the DBGame entity
            query.setParameter("game", dbGame);

            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }


    public static void delete(String id) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            DBGame game = entityManager.find(DBGame.class, id);
            if (game != null) {
                entityManager.remove(game);
            }
            transaction.commit();
        } catch (HibernateException ignored) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }
    }

}
