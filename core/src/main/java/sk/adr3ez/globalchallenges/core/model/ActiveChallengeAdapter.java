package sk.adr3ez.globalchallenges.core.model;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBGame;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayer;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.model.player.ChallengePlayer;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.core.database.PlayerDAO;
import sk.adr3ez.globalchallenges.core.database.PlayerDataDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final GlobalChallenges plugin = GlobalChallengesProvider.get();

    private Map<UUID, ChallengePlayer> players = new HashMap<>();
    private final Deque<ChallengePlayer> finishedPlayers = new LinkedList<>();

    @NotNull
    private Challenge challenge;

    @NotNull
    private Integer timeLeft;

    private final Long startTime = System.currentTimeMillis();
    @NotNull
    private final Double requiredScore;

    private final BukkitTask bossBarTask;
    private final BukkitTask timer;

    private final DBGame dbGame;


    public ActiveChallengeAdapter(@NotNull Challenge challenge, DBGame dbGame) {
        this.challenge = challenge;
        this.dbGame = dbGame;

        this.requiredScore = challenge.getRequiredScore();
        this.timeLeft = plugin.getConfiguration().getInt(ConfigRoutes.SETTINGS_CHALLENGE_TIME.getRoute());

        bossBarTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin.getJavaPlugin(), () -> {
            for (ChallengePlayer challengePlayer : players.values())
                challengePlayer.updateBossbar();
        }, 20, 20);
        this.timer = Bukkit.getScheduler().runTaskTimer(plugin.getJavaPlugin(), () -> {
            if (timeLeft <= 0)
                plugin.getGameManager().endActive();
            timeLeft -= 1;
        }, 0, 20);
    }

    @NotNull
    @Override
    public Challenge getChallenge() {
        return challenge;
    }

    @Override
    public Long getId() {
        return dbGame.getId();
    }

    @Override
    public Double getRequiredScore() {
        return requiredScore;
    }

    @Override
    public Integer getTimeLeft() {
        return timeLeft;
    }

    @Override
    public Long getStartTime() {
        return this.startTime;
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
        DBPlayer dbPlayer = PlayerDAO.findByUuid(uuid.toString());

        Bukkit.getLogger().warning("dbPlayer: " + dbPlayer.toString());
        Bukkit.getLogger().warning("dbGame: " + dbGame.toString());

        ChallengePlayer challengePlayer = new ChallengePlayer(uuid, audience,
                PlayerDataDAO.saveOrUpdate(new DBPlayerData(dbGame, dbPlayer, LocalDateTime.now()))
        );

        players.put(uuid, challengePlayer);
    }

    @Override
    public int countPlayers() {
        return players.size();
    }

    @NotNull
    @Override
    public boolean isJoined(@NotNull UUID uuid) {
        return this.players.containsKey(uuid);
    }

    @Override
    @ApiStatus.Internal
    public void handleEnd() {
        timer.cancel();
        bossBarTask.cancel();

        for (ChallengePlayer challengePlayer : players.values())
            challengePlayer.getAudience().hideBossBar(challengePlayer.getBossBar());

        challenge.handleEnd();
        players.clear();
        this.challenge = null;
        players = null;
    }

    @Override
    public void finishPlayer(UUID uuid) {
        ChallengePlayer challengePlayer = this.players.get(uuid);
        this.players.remove(uuid);

        this.finishedPlayers.add(challengePlayer);

        DBPlayerData playerData = challengePlayer.getDbPlayerData();

        playerData.setFinished(true);
        playerData.setTimeFinished(LocalDateTime.ofInstant(Instant.ofEpochMilli(challengePlayer.getFinishTime()),
                ZoneId.systemDefault()));

        PlayerDataDAO.saveOrUpdate(playerData);
    }
}
