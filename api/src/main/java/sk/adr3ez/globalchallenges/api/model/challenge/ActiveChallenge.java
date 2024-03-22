package sk.adr3ez.globalchallenges.api.model.challenge;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.player.ChallengePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    @NotNull
    Challenge getChallenge();

    Double getRequiredScore();

    List<UUID> getJoinedPlayers();

    Optional<ChallengePlayer> getPlayer(@NotNull UUID uuid);

    void joinPlayer(@NotNull UUID uuid, @NotNull Audience audience);

    void dumpPlayer(@NotNull UUID uuid);

    @NotNull
    int countPlayers();

    @NotNull
    boolean isJoined(@NotNull Player player);

    @NotNull
    boolean isJoined(@NotNull UUID uuid);

    void handleEnd();
}
