package sk.adr3ez.globalchallenges.core.util;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.api.util.log.PluginSettings;

public class PluginSettingsAdapter extends PluginSettings {


    int timeBetweenGames = 600; //Every 30 seconds TODO change
    int requiredPlayers = 2;
    @NotNull
    StorageMethod storageMethod = StorageMethod.SQLITE;

    public PluginSettingsAdapter(@NotNull GlobalChallenges plugin) {
        super(plugin);
    }

    @Override
    public int getTimeBetweenGames() {
        return timeBetweenGames;
    }

    @Override
    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    @NotNull
    @Override
    public StorageMethod getStorageMethod() {
        return storageMethod;
    }

    @NotNull
    @Override
    public String getDataHostname() {
        return plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_HOSTNAME.getRoute());
    }

    @NotNull
    @Override
    public String getDataPassword() {
        return plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_PASSWORD.getRoute());
    }

    @NotNull
    @Override
    public String getDataDatabase() {
        return plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_DATABASE.getRoute());
    }

    @NotNull
    @Override
    public String getDataUsername() {
        return plugin.getConfiguration().getString(ConfigRoutes.STORAGE_DATA_USERNAME.getRoute());
    }

    @Override
    public int getDataMinimumConnections() {
        return plugin.getConfiguration().getInt(ConfigRoutes.STORAGE_DATA_MINIMUMCONNECTIONS.getRoute());
    }

    @Override
    public int getDataMaximumConnections() {
        return plugin.getConfiguration().getInt(ConfigRoutes.STORAGE_DATA_MAXIMUMCONNECTIONS.getRoute());
    }

    @Override
    public int getDataConnectionTimeout() {
        return plugin.getConfiguration().getInt(ConfigRoutes.STORAGE_DATA_CONNECTIONTIMEOUT.getRoute());
    }

    @Override
    public boolean monitorBlocks() {
        return plugin.getConfiguration().getBoolean(ConfigRoutes.SETTINGS_MONITOR_BLOCKS.getRoute());
    }

}
