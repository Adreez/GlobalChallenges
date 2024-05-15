package sk.adr3ez.globalchallenges.api.model.challenge;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.player.ChallengePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    @NotNull
    Challenge getChallenge();

    /**
     * Getter for ID which is used in database
     *
     * @return Long id
     */
    Long getId();

    /**
     * Required score for each player to end their challenge
     *
     * @return double
     */
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
    List<UUID> getJoinedPlayers();

    Optional<ChallengePlayer> getPlayer(@NotNull UUID uuid);

    void joinPlayer(@NotNull UUID uuid, @NotNull Audience audience);

    @NotNull
    int countPlayers();

    @NotNull
    boolean isJoined(@NotNull UUID uuid);

    @ApiStatus.Internal
    void handleEnd();

    void finishPlayer(UUID uuid);
}
