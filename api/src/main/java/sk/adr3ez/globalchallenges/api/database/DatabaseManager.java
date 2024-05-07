package sk.adr3ez.globalchallenges.api.database;

import jakarta.persistence.EntityManager;

public interface DatabaseManager {

    EntityManager getEntityManager();

    void close();

}
