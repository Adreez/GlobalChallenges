package sk.adr3ez.globalchallenges.core.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.io.File;
import java.io.IOException;
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

    public GameManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;

        try {
            this.challengesFile = YamlDocument.create(new File(plugin.getDataDirectory() + "/challenges.yml"),
                    Objects.requireNonNull(getClass().getClassLoader().getResource("challenges.yml")).openStream());
        } catch (IOException e) {
            plugin.getPluginLogger().warn("There was error with loading challenges.yml, please try to reload the plugin \n" + e);
        }

        //Load all challenges in sk.adr3ez.globalchallenges.core.challenges
        Set<Class<? extends Challenge>> set = new HashSet<>();

        Reflections reflector = new Reflections("sk.adr3ez.globalchallenges.core.challenges");

        try {
            set = reflector.getSubTypesOf(Challenge.class);
        } catch (Exception ignored) {
        }

        for (Class<? extends Challenge> clazz : set) {
            try {
                Challenge challenge = clazz.getDeclaredConstructor(GameManager.class).newInstance(this);
                registerChallenge(challenge);
            } catch (Exception ignored) {
            }
        }
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

        activeChallenge = Optional.of(new ActiveChallengeAdapter(challenge));

        //TODO Broadcast message
        plugin.broadcast(MiniMessage.miniMessage().deserialize("""
                                
                                STARTING GAME
                                
                Desc: %description%
                Name: %name%
                Key: %key%
                                
                """
                .replaceAll("%description%", challenge.getDescription())
                .replaceAll("%name%", challenge.getName())
                .replaceAll("%key%", challenge.getKey())
        ));

        return true;
    }

    @Override
    public void endActive() {
        activeChallenge.ifPresent(activeChallenge -> activeChallenge.getChallenge().handleEnd());

        plugin.broadcast(MiniMessage.miniMessage().deserialize("""
                                
                Game has been ended! Thank you for playing
                                
                """));

        activeChallenge = Optional.empty();
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
