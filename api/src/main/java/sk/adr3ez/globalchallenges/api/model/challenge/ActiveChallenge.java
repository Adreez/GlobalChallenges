package sk.adr3ez.globalchallenges.api.model.challenge;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    @NotNull
    Challenge getChallenge();

    ChallengeData getChallengeData();

    List<UUID> getJoinedPlayers();

    Optional<UUID> getPlayer(@NotNull UUID uuid);

    void joinPlayer(@NotNull UUID uuid);

    void dumpPlayer(@NotNull UUID uuid);

    @NotNull
    int countPlayers();

    @NotNull
    boolean isJoined(@NotNull Player player);

    @NotNull
    boolean isJoined(@NotNull UUID uuid);

    void handleEnd();
}
