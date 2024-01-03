package sk.adr3ez.globalchallenges.core.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.database.Storage;
import sk.adr3ez.globalchallenges.core.database.adapter.MySQLStorageAdapter;
import sk.adr3ez.globalchallenges.core.database.adapter.SQLiteStorageAdapter;
import sk.adr3ez.globalchallenges.core.database.mysql.MySQLConnectionFactoryAdapter;
import sk.adr3ez.globalchallenges.core.database.sqlite.SQLiteConnectionFactoryAdapter;

public class DataManagerAdapter implements DataManager {

    @NotNull
    StorageMethod storageMethod;
    @NotNull
    Storage storage;

    @Nullable
    @Getter
    private MySQLConnectionFactoryAdapter poolManager;
    @NotNull
    private final GlobalChallenges plugin;

    public DataManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
        storageMethod = plugin.getPluginSettings().getStorageMethod();


        if (storageMethod == StorageMethod.MYSQL) {
            poolManager = new MySQLConnectionFactoryAdapter();
            poolManager.setPassword(plugin.getPluginSettings().getDataPassword());
            poolManager.setUsername(plugin.getPluginSettings().getDataUsername());
            poolManager.setHostname(plugin.getPluginSettings().getDataHostname());
            poolManager.setDatabase(plugin.getPluginSettings().getDataDatabase());
            poolManager.setConnectionTimeout(plugin.getPluginSettings().getDataConnectionTimeout());
            poolManager.setMaximumConnections(plugin.getPluginSettings().getDataMaximumConnections());
            poolManager.setMinimumConnections(plugin.getPluginSettings().getDataMinimumConnections());

            storage = new MySQLStorageAdapter(poolManager, plugin, "globalchallenges_data");
        } else {
            storage = new SQLiteStorageAdapter(new SQLiteConnectionFactoryAdapter(plugin), "globalchallenges_data");
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
