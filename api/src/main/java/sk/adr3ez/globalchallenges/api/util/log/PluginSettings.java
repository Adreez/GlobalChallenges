package sk.adr3ez.globalchallenges.api.util.log;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.configuration.StorageMethod;

public abstract class PluginSettings {

    @NotNull
    protected final GlobalChallenges plugin;

    public PluginSettings(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;
    }

    /**
     * This method will return time when next game should be run
     *
     * @return int - time in ticks
     */
    public abstract int getTimeBetweenGames();

    /**
     * Players required to start a game
     *
     * @return int - number of players
     */
    public abstract int getRequiredPlayers();

    /**
     * Method of the storage... SQLite or MySQL
     *
     * @return StorageMethod
     */
    @NotNull
    public abstract StorageMethod getStorageMethod();

    @NotNull
    public abstract String getDataHostname();

    @NotNull
    public abstract String getDataPassword();

    @NotNull
    public abstract String getDataDatabase();

    @NotNull
    public abstract String getDataUsername();

    public abstract int getDataMinimumConnections();

    public abstract int getDataMaximumConnections();

    public abstract int getDataConnectionTimeout();


}
