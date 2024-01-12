package sk.adr3ez.globalchallenges.api.database;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;

public interface DataManager {

    @NotNull
    StorageMethod getStorageMethod();

    @NotNull
    Storage getStorage();

    @NotNull
    ConnectionFactory getFactory();

}
