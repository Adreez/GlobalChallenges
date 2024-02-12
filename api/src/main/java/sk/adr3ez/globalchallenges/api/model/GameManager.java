package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public interface GameManager {

    @Nullable
    Optional<Challenge<?>> getActiveChallenge();

    @NotNull
    Set<Challenge<?>> getLoadedChallenges();

    void startRandom();

    void start(@NotNull Challenge<?> challenge);

    void startChallenge(@NotNull Challenge<?> challenge);

    @Nullable
    Challenge<?> getChallenge(@NotNull String key);

    @Nullable
    Challenge<?> getChallenge(@NotNull Class<? extends Challenge<?>> clazz);

    @Nullable
    YamlDocument getChallengesFile();

    boolean registerChallenge(@NotNull Challenge<?> challenge);

    boolean unregisterChallenge(@NotNull Challenge<?> challenge);

    boolean unregisterChallenge(@NotNull String key);


}
