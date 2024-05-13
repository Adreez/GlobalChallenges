package sk.adr3ez.globalchallenges.api.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public enum ConfigRoutes {

    STORAGE_METHOD("storage.method"),
    STORAGE_DATA_HOSTNAME("storage.data.hostname"),
    STORAGE_DATA_DATABASE("storage.data.database"),
    STORAGE_DATA_PORT("storage.data.port"),
    STORAGE_DATA_USERNAME("storage.data.username"),
    STORAGE_DATA_PASSWORD("storage.data.password"),
    STORAGE_DATA_MINIMUMCONNECTIONS("storage.data.minimumConnections"),
    STORAGE_DATA_MAXIMUMCONNECTIONS("storage.data.maximumConnections"),
    STORAGE_DATA_CONNECTIONTIMEOUT("storage.data.connectionTimeout"),

    SERVER_ID("server_identifier"),

    SETTINGS_REQUIRED_PLAYERS("settings.players_required"),
    SETTINGS_CHALLENGE_TIME("settings.challenge_time"),
    SETTINGS_MONITOR_BLOCKS("settings.monitor_blocks"),

    MESSAGES_BROADCAST_GAMESTART_CHAT("messages.broadcast.game_start.chat"),
    MESSAGES_BROADCAST_GAMESTART_TITLE("messages.broadcast.game_start.title.title"),
    MESSAGES_BROADCAST_GAMESTART_SUBTITLE("messages.broadcast.game_start.title.subtitle"),
    MESSAGES_BROADCAST_GAMESTART_FADEIN("messages.broadcast.game_start.title.fadein"),
    MESSAGES_BROADCAST_GAMESTART_STAY("messages.broadcast.game_start.title.stay"),
    MESSAGES_BROADCAST_GAMESTART_FADEOUT("messages.broadcast.game_start.title.fadeout"),

    MESSAGES_BROADCAST_GAMEEND_CHAT("messages.broadcast.game_end.chat"),
    MESSAGES_BROADCAST_GAMEEND_TITLE("messages.broadcast.game_end.title.title"),
    MESSAGES_BROADCAST_GAMEEND_SUBTITLE("messages.broadcast.game_end.title.subtitle"),
    MESSAGES_BROADCAST_GAMEEND_FADEIN("messages.broadcast.game_end.title.fadein"),
    MESSAGES_BROADCAST_GAMEEND_STAY("messages.broadcast.game_end.title.stay"),
    MESSAGES_BROADCAST_GAMEEND_FADEOUT("messages.broadcast.game_end.title.fadeout"),

    PLAYER_ACTIVE_BOSSBAR("messages.player.active_bossbar"),
    PLAYER_FINISHED_BOSSBAR("messages.player.finished_bossbar"),


    ;

    @NotNull
    private final String route;

    ConfigRoutes(@NotNull String route) {
        this.route = route;
    }


}
