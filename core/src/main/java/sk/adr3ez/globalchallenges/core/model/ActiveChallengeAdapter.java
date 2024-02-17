package sk.adr3ez.globalchallenges.core.model;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.Challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final List<UUID> players = new ArrayList<>();

    @NotNull
    private final Challenge<?> challenge;

    public ActiveChallengeAdapter(@NotNull Challenge<?> challenge) {
        this.challenge = challenge;
    }

    @NotNull
    @Override
    public Challenge<?> getChallenge() {
        return challenge;
    }

    @Override
    public List<UUID> getJoinedPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Optional<UUID> getPlayer(@NotNull UUID uuid) {
        if (players.contains(uuid))
            return Optional.of(players.get(players.indexOf(uuid)));
        else
            return Optional.empty();
    }

    @Override
    public boolean add(@NotNull UUID uuid) {
        return players.add(uuid);
    }

    @Override
    public boolean remove(@NotNull UUID uuid) {
        return players.remove(uuid);
    }

    @Override
    public int countJoinedPlayers() {
        return players.size();
    }

    @NotNull
    @Override
    public boolean isJoined(@NotNull Player player) {
        return isJoined(player.getUniqueId());
    }
    
    @NotNull
    @Override
    public boolean isJoined(@NotNull UUID uuid) {
        return players.contains(uuid);
    }
}
