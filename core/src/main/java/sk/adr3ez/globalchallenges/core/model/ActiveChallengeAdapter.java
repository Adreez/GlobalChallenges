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
import sk.adr3ez.globalchallenges.core.database.GameDAO;
import sk.adr3ez.globalchallenges.core.database.PlayerDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final GlobalChallenges plugin = GlobalChallengesProvider.get();

    private Map<UUID, ChallengePlayer> players = new HashMap<>();

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
        this.dbGame = GameDAO.saveOrUpdate(dbGame);

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

        ChallengePlayer challengePlayer = new ChallengePlayer(uuid, audience,
                new DBPlayerData(dbGame, dbPlayer, LocalDateTime.now()));

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

        int finished = 0;
        //Handle players that did not finish
        for (ChallengePlayer challengePlayer : players.values()) {
            finishPlayer(challengePlayer.getUuid());
            if (challengePlayer.finished())
                finished++;
        }

        challenge.handleEnd();
        players.clear();
        this.challenge = null;
        players = null;

        dbGame.setEndTime(LocalDateTime.now());
        dbGame.setPlayersJoined(players.size());
        dbGame.setPlayersFinished(finished);

        GameDAO.saveOrUpdate(dbGame);
    }

    //Count for the position in which player has finished
    //Starting at 1st
    private int finishCount = 1;

    @Override
    public void finishPlayer(UUID uuid) {
        ChallengePlayer challengePlayer = this.players.get(uuid);
        challengePlayer.getAudience().hideBossBar(challengePlayer.getBossBar());
        this.players.remove(uuid);

        DBPlayerData playerData = challengePlayer.getDbPlayerData();

        if (challengePlayer.finished()) {

            playerData.setPosition(finishCount);
            playerData.setFinished(true);
            playerData.setTimeFinished(LocalDateTime.ofInstant(Instant.ofEpochMilli(challengePlayer.getFinishTime()),
                    ZoneId.systemDefault()));
            finishCount++;

        }

        DBPlayer dbPlayer = PlayerDAO.findByUuid(uuid);
        dbPlayer.addPlayerData(playerData);
        Bukkit.broadcastMessage(dbPlayer.toString());
        PlayerDAO.saveOrUpdate(dbPlayer);

    }
}
