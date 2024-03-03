package sk.adr3ez.globalchallenges.api.database;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;

public interface DataManager {

    /**
     * Simple enum return value
     *
     * @return StorageMethod
     */
    @NotNull
    StorageMethod getStorageMethod();

    /**
     * This method will provide you methods for storage
     *
     * @return Storage instance
     */
    @NotNull
    Storage getStorage();

    /**
     * Gets the connection to the database
     *
     * @return ConnectionFactory instance
     */
    @NotNull
    ConnectionFactory getFactory();

}
