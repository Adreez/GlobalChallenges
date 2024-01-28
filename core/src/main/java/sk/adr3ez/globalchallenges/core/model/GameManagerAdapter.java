package sk.adr3ez.globalchallenges.core.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.model.Challenge;
import sk.adr3ez.globalchallenges.api.model.GameManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameManagerAdapter implements GameManager {

    @NotNull
    private final GlobalChallenges plugin;

    @Nullable
    private YamlDocument challengesFile;
    @Nullable
    private Challenge<?> activeChallenge;

    @NotNull
    private final List<Challenge<?>> registeredChallenges = new ArrayList<>();

    public GameManagerAdapter(@NotNull GlobalChallenges plugin) {
        this.plugin = plugin;

        try {
            this.challengesFile = YamlDocument.create(new File(plugin.getDataDirectory() + "/challenges.yml"),
                    Objects.requireNonNull(getClass().getClassLoader().getResource("challenges.yml")).openStream());
        } catch (IOException e) {
            plugin.getPluginLogger().warn("There was error with loading challenges.yml, please try to reload the plugin \n" + e);
        }
    }

    @Nullable
    @Override
    public Challenge<?> getActiveChallenge() {
        return activeChallenge;
    }

    @Override
    public void startRandom() {
        Random random = new Random();
        int rand = random.nextInt(registeredChallenges.size());

        Challenge<?> challenge = registeredChallenges.get(rand);

        if (challenge.start(challengesFile, "challenges." + challenge.getKey())) {
            activeChallenge = challenge;
        }
    }

    @Override
    public void startChallenge(@NotNull Challenge<?> challenge) {
        if (activeChallenge == null)
            challenge.start(challengesFile, "challenges." + challenge.getKey());
    }

    @Nullable
    @Override
    public Challenge<?> getChallenge(@NotNull String key) {
        for (Challenge<?> challenge : registeredChallenges) {
            if (challenge.getKey().equals(key)) {
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
    public boolean registerChallenge(@NotNull Challenge<?> challenge) {
        registeredChallenges.add(challenge);
        return true;
    }

    @Override
    public boolean unregisterChallenge(@NotNull Challenge<?> challenge) {
        if (registeredChallenges.contains(challenge)) {
            registeredChallenges.remove(challenge);
            return true;
        }
        return false;
    }

    @Override
    public boolean unregisterChallenge(@NotNull String key) {
        for (Challenge<?> challenge : registeredChallenges) {
            if (challenge.getKey().equals(key)) {
                return unregisterChallenge(challenge);
            }
        }
        return false;
    }

}
