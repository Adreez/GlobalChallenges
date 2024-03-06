package sk.adr3ez.globalchallenges.core.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.model.challenge.ChallengeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private List<UUID> players;

    @NotNull
    private Challenge challenge;
    private ChallengeData challengeData;

    public ActiveChallengeAdapter(@NotNull Challenge challenge) {
        this.challenge = challenge;
        this.players = new ArrayList<>();
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

        Bukkit.getScheduler().runTaskAsynchronously(GlobalChallengesProvider.get().getJavaPlugin(),
                () -> GlobalChallengesProvider.get().getDataManager().getStorage().addJoin(uuid));
    }

    @Override
    @ApiStatus.Internal
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

    @Override
    public void handleEnd() {
        challenge.handleEnd();
        players.clear();
        this.challengeData = null;
        this.challenge = null;
        players = null;
    }
}
