package sk.adr3ez.globalchallenges.core.model;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.model.challenge.ChallengeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final List<UUID> players = new ArrayList<>();

    @NotNull
    private final Challenge challenge;
    private final ChallengeData challengeData;

    public ActiveChallengeAdapter(@NotNull Challenge challenge) {
        this.challenge = challenge;

        challengeData = new ChallengeData(this);
    }

    @NotNull
    @Override
    public Challenge getChallenge() {
        return challenge;
    }

    @Override
    public ChallengeData getChallengeData() {
        return challengeData;
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
    public void joinPlayer(@NotNull UUID uuid) {
        players.add(uuid);
    }

    @Override
    public void dumpPlayer(@NotNull UUID uuid) {
        players.remove(uuid);
    }

    @Override
    public int countPlayers() {
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
