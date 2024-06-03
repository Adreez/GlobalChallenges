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
import sk.adr3ez.globalchallenges.api.model.player.ActivePlayer;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.core.database.dao.GameDAO;
import sk.adr3ez.globalchallenges.core.database.dao.PlayerDAO;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final GlobalChallenges plugin = GlobalChallengesProvider.get();

    private Map<UUID, ActivePlayer> players = new HashMap<>();
    private Map<UUID, ActivePlayer> finishedPlayers = new HashMap<>();

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

        if (challenge.canBeUnlimited()) {
            if (Math.random() > 0.5) {
                requiredScore = -1.0;
            } else
                this.requiredScore = challenge.getRequiredScore();
        } else {
            this.requiredScore = challenge.getRequiredScore();
        }

        this.timeLeft = plugin.getConfiguration().getInt(ConfigRoutes.SETTINGS_CHALLENGE_TIME.getRoute());

        bossBarTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin.getJavaPlugin(), () -> {
            for (ActivePlayer activePlayer : players.values())
                activePlayer.updateBossbar();
            for (ActivePlayer activePlayer : finishedPlayers.values())
                activePlayer.updateBossbar();
        }, 20, 20);
        this.timer = Bukkit.getScheduler().runTaskTimer(plugin.getJavaPlugin(), () -> {
            if (timeLeft <= 0) plugin.getGameManager().endActive();
            timeLeft -= 1;
        }, 0, 20);
    }

    @NotNull
    @Override
    public Challenge getChallenge() {
        return this.challenge;
    }

    @Override
    public DBGame getDbGame() {
        return this.dbGame;
    }

    @Override
    public Double getRequiredScore() {
        return this.requiredScore;
    }

    @Override
    public Integer getTimeLeft() {
        return this.timeLeft;
    }

    @Override
    public Long getStartTime() {
        return this.startTime;
    }

    @Override
    public List<ActivePlayer> getJoinedPlayers() {
        ArrayList<ActivePlayer> list = new ArrayList<>(players.values());
        list.addAll(finishedPlayers.values());
        return list;
    }

    @Override
    public Optional<ActivePlayer> getPlayer(@NotNull UUID uuid) {
        if (players.containsKey(uuid))
            return Optional.of(players.get(uuid));
        else if (finishedPlayers.containsKey(uuid))
            return Optional.of(finishedPlayers.get(uuid));
        return Optional.empty();
    }

    @Override
    public void joinPlayer(@NotNull UUID uuid, @NotNull String name, @NotNull Audience audience) {
        DBPlayer dbPlayer = PlayerDAO.findByUuid(uuid.toString());

        ActivePlayer activePlayer = new ActivePlayer(uuid, name, audience,
                new DBPlayerData(dbGame, dbPlayer, Timestamp.from(Instant.now())),
                this);

        players.put(uuid, activePlayer);
    }

    @Override
    public int countPlayers() {
        return players.size() + finishedPlayers.size();
    }

    @NotNull
    @Override
    public boolean isJoined(@NotNull UUID uuid) {
        return this.players.containsKey(uuid) || this.finishedPlayers.containsKey(uuid);
    }

    @NotNull
    @Override
    public boolean isFinished(@NotNull UUID uuid) {
        return false;
    }

    @Override
    @ApiStatus.Internal
    public void handleEnd() {
        timer.cancel();
        bossBarTask.cancel();

        ArrayList<ActivePlayer> list = new ArrayList<>(players.values());
        list.sort(Comparator.comparingDouble(ActivePlayer::getScore).reversed());
        if (requiredScore == -1) {

            for (ActivePlayer activePlayer : list) {
                activePlayer.setFinished(activePlayer.getScore() > 0);
                activePlayer.setFinishTime(System.currentTimeMillis());
                finishPlayer(activePlayer.getUuid());
            }
        }
        //Handle players that did not finish
        list.addAll(finishedPlayers.values());
        for (ActivePlayer activePlayer : list) {
            UUID uuid = activePlayer.getUuid();

            if (!finishedPlayers.containsKey(uuid))
                finishPlayer(uuid);

            activePlayer.getAudience().hideBossBar(activePlayer.getBossBar());
        }


        dbGame.setEndTime(LocalDateTime.now());
        dbGame.setPlayersJoined(players.size() + finishedPlayers.size());
        dbGame.setPlayersFinished(finishedPlayers.size());

        GameDAO.saveOrUpdate(dbGame);

        for (ActivePlayer activePlayer : finishedPlayers.values()) {
            if (activePlayer.finished()) {
                if (plugin.getConfiguration().getInt("rewards.position." + activePlayer.getDbPlayerData().getPosition()) != null) {
                    for (String s : plugin.getConfiguration().getStringList("rewards.position." + activePlayer.getDbPlayerData().getPosition())) {
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replaceAll("%player%", activePlayer.getName()));
                    }
                }
            }

            for (String s : plugin.getConfiguration().getStringList("rewards.join")) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), s.replaceAll("%player%", activePlayer.getName()));
            }
        }

        challenge.handleEnd();
        this.challenge = null;
        players = null;
        finishedPlayers = null;
    }

    //Count for the position in which player has finished
    //Starting at 1st
    private int finishCount = 1;

    @Override
    public void finishPlayer(UUID uuid) {
        ActivePlayer activePlayer = this.players.get(uuid);
        players.remove(uuid);

        DBPlayerData playerData = activePlayer.getDbPlayerData();

        if (activePlayer.finished()) {
            playerData.setPosition(finishCount);
            playerData.setFinished(true);
            playerData.setTimeFinished(Timestamp.from(Instant.ofEpochMilli(activePlayer.getFinishTime())));
            finishCount++;
        }

        activePlayer.setDbPlayerData(playerData);

        DBPlayer dbPlayer = PlayerDAO.findByUuid(uuid);
        dbPlayer.addPlayerData(playerData);
        PlayerDAO.saveOrUpdate(dbPlayer);

        finishedPlayers.put(uuid, activePlayer);
    }
}