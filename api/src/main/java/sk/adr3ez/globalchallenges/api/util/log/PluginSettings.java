package sk.adr3ez.globalchallenges.api.util.log;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;

public abstract class PluginSettings {

    @NotNull
    private final GlobalChallenges plugin;

    public PluginSettings(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
    }

    /**
     * This method will return time when next game should be run
     * @return int - time in ticks
     */
    public abstract int getTimeBetweenGames();

    /**
     * Players required to start a game
     * @return int - number of players
     */
    public abstract int getRequiredPlayers();

    /**
     * Method of the storage... SQLite or MySQL
     * @return StorageMethod
     */
    @NotNull
    public abstract StorageMethod getStorageMethod();

}
