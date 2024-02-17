package sk.adr3ez.globalchallenges.api.model;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    @NotNull
    Challenge<?> getChallenge();

    List<UUID> getJoinedPlayers();

    Optional<UUID> getPlayer(@NotNull UUID uuid);

    boolean add(@NotNull UUID uuid);

    boolean remove(@NotNull UUID uuid);

    @NotNull
    int countJoinedPlayers();

    @NotNull
    boolean isJoined(@NotNull Player player);

    @NotNull
    boolean isJoined(@NotNull UUID uuid);
}
