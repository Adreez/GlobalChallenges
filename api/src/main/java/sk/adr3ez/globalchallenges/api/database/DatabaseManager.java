package sk.adr3ez.globalchallenges.api.database;

import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.database.entity.DBServer;
import sk.adr3ez.globalchallenges.api.util.StorageMethod;

public interface DatabaseManager {

    EntityManager getEntityManager();

    StorageMethod getStorageMethod();

    /**
     * Get DBServer object
     *
     * @return DBServer
     */
    @NotNull
    DBServer getDatabaseServer();

    void close();

}
