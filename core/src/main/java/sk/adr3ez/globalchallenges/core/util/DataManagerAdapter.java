package sk.adr3ez.globalchallenges.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.database.Storage;

public class DataManagerAdapter implements DataManager {

    @Nullable
    StorageMethod storageMethod;
    @Nullable
    Storage storage;
    @Nullable
    private GlobalChallenges plugin;

    public DataManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
        storageMethod = plugin.getPluginSettings().getStorageMethod();

        switch (storageMethod) {
            case MYSQL -> {
                storage =
            }
            case SQLITE -> {

            }
        }

    }

    @NotNull
    @Override
    public StorageMethod getStorageMethod() {
        return storageMethod;
    }

    @NotNull
    @Override
    public Storage getStorage() {
        return storage;
    }
}
