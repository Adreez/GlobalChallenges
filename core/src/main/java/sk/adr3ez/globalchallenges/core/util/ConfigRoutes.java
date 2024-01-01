package sk.adr3ez.globalchallenges.core.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ConfigRoutes {

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
