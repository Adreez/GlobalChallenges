package sk.adr3ez.globalchallenges.api.database;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Storage {

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    boolean createUser(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    boolean exists(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    boolean deleteUser(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     */
    void addJoin(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    boolean addWin(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    int getWins(@NotNull UUID uuid);

    /**
     * @param uuid of the player.
     * @return if operation was successful
     */
    int getJoins(@NotNull UUID uuid);

}
