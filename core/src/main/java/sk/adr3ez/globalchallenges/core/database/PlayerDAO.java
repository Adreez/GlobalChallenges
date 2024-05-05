package sk.adr3ez.globalchallenges.core.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import org.bukkit.Bukkit;
import org.hibernate.jpa.HibernatePersistenceProvider;
import sk.adr3ez.globalchallenges.core.database.entity.DBPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDAO {

    private final EntityManagerFactory entityManagerFactory;

    public PlayerDAO() {
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update"); // create, none, update
        this.entityManagerFactory = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new CustomPersistenceUnitInfo("GlobalChallenges"), props);
    }

    public void saveOrUpdate(DBPlayer player) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
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

    public DBPlayer findById(String uuid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(DBPlayer.class, uuid);
        } finally {
            entityManager.close();
        }
    }

    public List<DBPlayer> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM Player p", DBPlayer.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void delete(String uuid) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
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

    public void close() {
        entityManagerFactory.close();
    }
}
