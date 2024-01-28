package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenge<T extends Number> {

    @NotNull
    String getKey();

    @NotNull
    String getName();

    @NotNull
    String getDescription();

    boolean isEnabled();

    /**
     * @param document YamlDocument configuration of the challenge
     * @param path     The path to the challenge in document
     * @return if game was successfuly started
     */
    boolean start(@Nullable YamlDocument document, @Nullable String path);

    void end();

    void addScore(@NotNull T value, @NotNull UUID target);

    void setScore(@NotNull T value, @NotNull UUID target);

    @Nullable
    T getScore(@NotNull UUID target);

}
