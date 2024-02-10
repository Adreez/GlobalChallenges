package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GameManager {

    @Nullable
    Challenge<?> getActiveChallenge();

    void startRandom();

    void start(@NotNull Challenge<?> challenge);

    void startChallenge(@NotNull Challenge<?> challenge);

    @Nullable
    Challenge<?> getChallenge(@NotNull String key);

    @Nullable
    YamlDocument getChallengesFile();

    boolean registerChallenge(@NotNull Challenge<?> challenge);

    boolean unregisterChallenge(@NotNull Challenge<?> challenge);

    boolean unregisterChallenge(@NotNull String key);


}
