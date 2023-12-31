package sk.adr3ez.globalchallenges.core.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;
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

    @Getter
    public static enum ConfigRoutes {

        STORAGE_METHOD("storage.method"),
        STORAGE_DATA_HOSTNAME("storage.data.hostname"),
        STORAGE_DATA_DATABASE("storage.data.database"),
        STORAGE_DATA_USERNAME("storage.data.username"),
        STORAGE_DATA_PASSWORD("storage.data.password"),
        STORAGE_DATA_MINIMUMCONNECTIONS("storage.data.minimumConnections"),
        STORAGE_DATA_MAXIMUMCONNECTIONS("storage.data.maximumConnections"),
        STORAGE_DATA_CONNECTIONTIMEOUT("storage.data.connectionTimeout"),


        ;

        @NotNull
        private final String route;

        ConfigRoutes(@NotNull String route) {
            this.route = route;
        }


    }

}
