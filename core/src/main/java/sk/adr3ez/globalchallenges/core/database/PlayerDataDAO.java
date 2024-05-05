package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import sk.adr3ez.globalchallenges.core.database.entity.DBPlayer;

public class PlayerDataDAO {

    public void saveOrUpdate(DBPlayer DBPlayer) {
        EntityManager entityManager = DatabaseManager.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.merge(DBPlayer);
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle or log the exception appropriately
        } finally {
            entityManager.close();
        }
    }

    public DBPlayer findByUUID(String uuid) {
        EntityManager entityManager = DatabaseManager.getEntityManager();

        DBPlayer DBPlayer = null;
        try {
            DBPlayer = entityManager.find(DBPlayer.class, uuid);
        } catch (Exception ignored) {

        } finally {
            entityManager.close();
        }
        return DBPlayer;
    }

    public void delete(DBPlayer DBPlayer) {
        EntityManager entityManager = DatabaseManager.getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(DBPlayer) ? DBPlayer : entityManager.merge(DBPlayer));
            entityManager.getTransaction().commit();
        } catch (Exception ignored) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            entityManager.close();
        }
    }
}
