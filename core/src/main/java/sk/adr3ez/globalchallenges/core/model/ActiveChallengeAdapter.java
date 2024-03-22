package sk.adr3ez.globalchallenges.core.model;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.model.player.ChallengePlayer;

import java.util.*;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private HashMap<UUID, ChallengePlayer> players;

    @NotNull
    private Challenge challenge;

    private Double requiredScore;


    public ActiveChallengeAdapter(@NotNull Challenge challenge) {
        this.challenge = challenge;
        this.players = new HashMap<>();
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
        ChallengePlayer cp = new ChallengePlayer();
        cp.setBossBar(BossBar.bossBar(Component.text("Yey"), 0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS));

        GlobalChallenges plugin = GlobalChallengesProvider.get();
        players.put(uuid, cp);

        audience.showBossBar(cp.getBossBar());

        Bukkit.getScheduler().runTaskAsynchronously(plugin.getJavaPlugin(),
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
        return players.containsKey(uuid);
    }

    @Override
    public void handleEnd() {
        challenge.handleEnd();
        players.clear();
        this.challenge = null;
        players = null;
    }
}
