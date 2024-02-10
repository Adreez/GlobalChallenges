package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class Challenge<T extends Number> {

    @NotNull
    protected YamlDocument document;

    public Challenge(@NotNull YamlDocument document) {
        this.document = document;
    }

    @NotNull
    public abstract String getKey();

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract String getDescription();

    public abstract boolean isEnabled();

    /**
     * @param document YamlDocument configuration of the challenge
     * @param path     The path to the challenge in document
     * @return if game was successfuly started
     */
    public abstract boolean start();

    public abstract void end();

    public abstract void addScore(@NotNull T value, @NotNull UUID target);

    public abstract void setScore(@NotNull T value, @NotNull UUID target);

    @Nullable
    public abstract T getScore(@NotNull UUID target);

}
