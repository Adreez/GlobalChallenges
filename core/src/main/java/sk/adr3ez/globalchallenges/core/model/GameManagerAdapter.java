package sk.adr3ez.globalchallenges.core.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.entity.DBGame;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class GameManagerAdapter implements GameManager {

    @NotNull
    private final GlobalChallenges plugin;

    @Nullable
    private YamlDocument challengesFile;
    @Nullable
    private Optional<ActiveChallenge> activeChallenge = Optional.empty();

    @NotNull
    private final List<Challenge> registeredChallenges = new ArrayList<>();

    private final BukkitTask autoStartTask; //TODO ADD SHUTDOWN ACTION

    public GameManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;

        try {
            this.challengesFile = YamlDocument.create(new File(plugin.getDataDirectory() + "/challenges.yml"),
                    Objects.requireNonNull(getClass().getClassLoader().getResource("challenges.yml")).openStream());
        } catch (IOException e) {
            plugin.getPluginLogger().warn("There was error with loading challenges.yml, please try to reload the plugin \n" + e);
        }

        //Load all challenges in sk.adr3ez.globalchallenges.core.challenges
        Set<Class<? extends Challenge>> classes = new HashSet<>();

        Reflections reflector = new Reflections("sk.adr3ez.globalchallenges.core.challenges");
        try {
            classes = reflector.getSubTypesOf(Challenge.class);
        } catch (Exception ignored) {
        }

        for (Class<? extends Challenge> clazz : classes) {
            try {
                Challenge challenge = clazz.getDeclaredConstructor(GameManager.class).newInstance(this);
                registerChallenge(challenge);
            } catch (Exception ignored) {
            }
        }

        long autoStartTime = plugin.getConfiguration().getLong(ConfigRoutes.SETTINGS_AUTO_START.getRoute(), 900L) * 20;
        autoStartTask = Bukkit.getScheduler().runTaskTimer(plugin.getJavaPlugin(), () -> {

            if (getActiveChallenge().isPresent()) {
                plugin.getPluginLogger().info("Automatic challenge cannot be started because there is already an active challenge.");
                return;
            }

            if (plugin.getOnlinePlayers().isEmpty() || plugin.getOnlinePlayers().size() < plugin.getConfiguration().getInt(ConfigRoutes.SETTINGS_REQUIRED_PLAYERS.getRoute())) {
                plugin.getPluginLogger().info("Not enough players online to start a game.");
                return;
            }
            startRandom();

        }, autoStartTime, autoStartTime);
    }

    @Nullable
    @Override
    public Optional<ActiveChallenge> getActiveChallenge() {
        return activeChallenge;
    }

    @Override
    public @NotNull Set<Challenge> getLoadedChallenges() {
        return new HashSet<>(registeredChallenges);
    }

    @Override
    public Set<String> getLoadedChallengesKeys() {
        Set<String> keys = new HashSet<>();
        for (Challenge challenge : getLoadedChallenges()) {
            keys.add(challenge.getKey());
        }
        return keys;
    }

    @Override
    public void startRandom() {

        if (registeredChallenges.isEmpty())
            return;

        Random random = new Random();
        int rand = random.nextInt(registeredChallenges.size());

        this.start(registeredChallenges.get(rand));
    }

    @Override
    public boolean start(@NotNull Challenge challenge) {

        if (activeChallenge.isPresent()) //Won't start challenge if one is active
            return false;

        if (!challenge.handleStart())
            return false;

        DBGame dbGame = new DBGame(challenge.getKey(), challenge.getDescription(), Timestamp.valueOf(LocalDateTime.now()));
        activeChallenge = Optional.of(new ActiveChallengeAdapter(challenge, dbGame));

        plugin.broadcast(MiniMessage.miniMessage().deserialize(String.join("<br>", plugin.getConfiguration().getStringList(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_CHAT.getRoute()))
                .replaceAll("%description%", challenge.getDescription())
                .replaceAll("%name%", challenge.getName())
                .replaceAll("%key%", challenge.getKey())
        ));
        plugin.broadcastTitle(Title.title(
                MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_TITLE.getRoute())),
                MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_SUBTITLE.getRoute())),
                Title.Times.times(
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_FADEIN.getRoute())),
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_STAY.getRoute())),
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMESTART_FADEOUT.getRoute()))
                )));

        return true;
    }

    @Override
    public void endActive() {
        this.activeChallenge.get().handleEnd();

        plugin.broadcast(MiniMessage.miniMessage().deserialize(String.join("<br>",
                plugin.getConfiguration().getStringList(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_CHAT.getRoute()))
        ));
        plugin.broadcastTitle(Title.title(
                MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_TITLE.getRoute())),
                MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_SUBTITLE.getRoute())),
                Title.Times.times(
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_FADEIN.getRoute())),
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_STAY.getRoute())),
                        Duration.ofMillis(plugin.getConfiguration().getInt(ConfigRoutes.MESSAGES_BROADCAST_GAMEEND_FADEOUT.getRoute()))
                )));

        this.activeChallenge = Optional.empty();
    }

    @Nullable
    @Override
    public Challenge getChallenge(@NotNull String key) {
        if (getLoadedChallengesKeys().contains(key)) {
            for (Challenge challenge : registeredChallenges) {
                if (challenge.getKey().equals(key)) {
                    return challenge;
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Challenge getChallenge(@NotNull Class<? extends Challenge> clazz) {
        for (Challenge challenge : registeredChallenges) {
            if (clazz.isAssignableFrom(challenge.getClass())) {
                return challenge;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public YamlDocument getChallengesFile() {
        return challengesFile;
    }

    @Override
    public void registerChallenge(@NotNull Challenge challenge) {
        if (challenge.canLoad()) {
            registeredChallenges.add(challenge);
        }
    }

    @Override
    public boolean unregisterChallenge(@NotNull Challenge challenge) {
        if (registeredChallenges.contains(challenge)) {
            registeredChallenges.remove(challenge);
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterChallenge(@NotNull String key) {
        for (Challenge challenge : registeredChallenges) {
            if (challenge.getKey().equals(key)) {
                return unregisterChallenge(challenge);
            }
        }
        return false;
    }

}
