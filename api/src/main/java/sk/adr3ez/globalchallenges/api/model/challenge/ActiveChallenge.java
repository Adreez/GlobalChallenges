package sk.adr3ez.globalchallenges.api.model.challenge;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.database.entity.DBGame;
import sk.adr3ez.globalchallenges.api.model.player.ActivePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    @NotNull
    Challenge getChallenge();

    /**
     * Getter database object of running game
     *
     * @return Long id
     */
    DBGame getDbGame();

    Double getRequiredScore();

    /**
     * Time in seconds
     *
     * @return integer
     */
    Integer getTimeLeft();

    /**
     * Time in millis
     *
     * @return long
     */
    Long getStartTime();

    /**
     * List of all players that joined challenge
     *
     * @return List<UUID>
     */
    List<ActivePlayer> getJoinedPlayers();

    Optional<ActivePlayer> getPlayer(@NotNull UUID uuid);

    void joinPlayer(@NotNull UUID uuid, @NotNull Audience audience);

    void finishPlayer(UUID uuid);

    @NotNull
    int countPlayers();

    @NotNull
    boolean isJoined(@NotNull UUID uuid);

    @NotNull
    boolean isFinished(@NotNull UUID uuid);

    void handleEnd();
}
