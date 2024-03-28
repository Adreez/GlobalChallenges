package sk.adr3ez.globalchallenges.core.model;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.model.player.ChallengePlayer;

import java.util.*;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private Map<UUID, ChallengePlayer> players = new HashMap<>();

    @NotNull
    private Challenge challenge;

    private final Double requiredScore;

    private BukkitTask bossBarTask;


    public ActiveChallengeAdapter(@NotNull Challenge challenge) {
        this.challenge = challenge;

        this.requiredScore = challenge.getRequiredScore();
        Bukkit.getServer().broadcastMessage("Players: " + players.keySet());
    }

    @NotNull
    @Override
    public Challenge getChallenge() {
        return challenge;
    }

    @Override
    public Double getRequiredScore() {
        return requiredScore;
    }

    @Override
    public List<UUID> getJoinedPlayers() {
        return new ArrayList<>(players.keySet());
    }

    @Override
    public Optional<ChallengePlayer> getPlayer(@NotNull UUID uuid) {
        if (players.containsKey(uuid))
            return Optional.of(players.get(uuid));
        return Optional.empty();
    }

    @Override
    public void joinPlayer(@NotNull UUID uuid, @NotNull Audience audience) {
        ChallengePlayer cp = new ChallengePlayer(uuid, audience);

        GlobalChallenges plugin = GlobalChallengesProvider.get();
        players.put(uuid, cp);

        Bukkit.getScheduler().runTaskAsynchronously(plugin.getJavaPlugin(),
                () -> GlobalChallengesProvider.get().getDataManager().getStorage().addJoin(uuid));

        bossBarTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin.getJavaPlugin(), () -> {
            for (ChallengePlayer challengePlayer : players.values())
                challengePlayer.updateBossbar();
        }, 20, 20);

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
        return this.isJoined(player.getUniqueId());
    }

    @NotNull
    @Override
    public boolean isJoined(@NotNull UUID uuid) {
        return this.players.containsKey(uuid);
    }

    @Override
    public void handleEnd() {
        for (ChallengePlayer challengePlayer : players.values())
            challengePlayer.getAudience().hideBossBar(challengePlayer.getBossBar());
        bossBarTask.cancel();

        challenge.handleEnd();
        players.clear();
        this.challenge = null;
        players = null;
    }
}
