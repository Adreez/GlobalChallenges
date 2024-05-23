package sk.adr3ez.globalchallenges.api.database;

import jakarta.persistence.EntityManager;
import sk.adr3ez.globalchallenges.api.util.StorageMethod;

public interface DatabaseManager {

    EntityManager getEntityManager();

    StorageMethod getStorageMethod();

    void close();

}
