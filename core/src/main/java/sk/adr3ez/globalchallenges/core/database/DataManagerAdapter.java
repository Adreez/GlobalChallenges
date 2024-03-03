package sk.adr3ez.globalchallenges.core.database;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;
import sk.adr3ez.globalchallenges.api.database.ConnectionFactory;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.database.Storage;
import sk.adr3ez.globalchallenges.core.database.adapter.StorageAdapter;
import sk.adr3ez.globalchallenges.core.database.mysql.MySQLConnectionFactoryAdapter;
import sk.adr3ez.globalchallenges.core.database.sqlite.SQLiteConnectionFactoryAdapter;

public class DataManagerAdapter implements DataManager {

    @NotNull
    StorageMethod storageMethod;
    @NotNull
    Storage storage;

    @NotNull
    private final ConnectionFactory connectionFactory;

    @NotNull
    private final GlobalChallenges plugin;

    public DataManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
        storageMethod = plugin.getPluginSettings().getStorageMethod();

        if (storageMethod == StorageMethod.MYSQL) {

            connectionFactory = new MySQLConnectionFactoryAdapter();
            MySQLConnectionFactoryAdapter connFact = (MySQLConnectionFactoryAdapter) connectionFactory;

            connFact.setPassword(plugin.getPluginSettings().getDataPassword());
            connFact.setUsername(plugin.getPluginSettings().getDataUsername());
            connFact.setHostname(plugin.getPluginSettings().getDataHostname());
            connFact.setDatabase(plugin.getPluginSettings().getDataDatabase());
            connFact.setConnectionTimeout(plugin.getPluginSettings().getDataConnectionTimeout());
            connFact.setMaximumConnections(plugin.getPluginSettings().getDataMaximumConnections());
            connFact.setMinimumConnections(plugin.getPluginSettings().getDataMinimumConnections());
            connFact.setup();

        } else {
            connectionFactory = new SQLiteConnectionFactoryAdapter(plugin);
        }

        storage = new StorageAdapter(connectionFactory, plugin, "globalchallenges_data");
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

    @NotNull
    @Override
    public ConnectionFactory getFactory() {
        return connectionFactory;
    }
}
