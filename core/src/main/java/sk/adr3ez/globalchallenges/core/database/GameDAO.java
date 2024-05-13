package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.core.database.entity.DBGame;

import java.util.List;

public final class GameDAO {

    private static final GlobalChallenges globalChallenges;

    static {
        globalChallenges = GlobalChallengesProvider.get();
    }

    public static void saveOrUpdate(DBGame game) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(game);
            transaction.commit();
        } catch (PersistenceException ignored) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
        }
    }

    public static DBGame findById(String id) {
        EntityManager entityManager = globalChallenges.getDatabaseManager().getEntityManager();
        try {
            return entityManager.find(DBGame.class, id);
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
