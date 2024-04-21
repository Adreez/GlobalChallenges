package sk.adr3ez.globalchallenges.core.database;

import javax.persistence.EntityManager;

public class PlayerDataDAO {

    public void saveOrUpdate(PlayerData playerData) {
        EntityManager entityManager = new DatabaseManager().getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(playerData);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public PlayerData findByUUID(String uuid) {
        EntityManager entityManager = new DatabaseManager().getEntityManager();

        PlayerData playerData = null;
        try {
            playerData = entityManager.find(PlayerData.class, uuid);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return playerData;
    }

    public void delete(PlayerData playerData) {
        EntityManager entityManager = new DatabaseManager().getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(playerData) ? playerData : entityManager.merge(playerData));
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }
}
