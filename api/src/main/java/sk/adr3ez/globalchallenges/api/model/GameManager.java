package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.Optional;
import java.util.Set;

public interface GameManager {

    @Nullable
    Optional<ActiveChallenge> getActiveChallenge();

    @NotNull
    Set<Challenge> getLoadedChallenges();

    Set<String> getLoadedChallengesKeys();

    void startRandom();

    boolean start(@NotNull Challenge challenge);

    void endActive();


    @Nullable
    Challenge getChallenge(@NotNull String key);

    @Nullable
    Challenge getChallenge(@NotNull Class<? extends Challenge> clazz);

    @Nullable
    YamlDocument getChallengesFile();

    void registerChallenge(@NotNull Challenge challenge);

    boolean unregisterChallenge(@NotNull Challenge challenge);

    boolean unregisterChallenge(@NotNull String key);


}
