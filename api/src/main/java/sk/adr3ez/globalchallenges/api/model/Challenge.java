package sk.adr3ez.globalchallenges.api.model;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;

public abstract class Challenge<T extends Number> implements Scoreable<T> {

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

    public abstract void start();

    public abstract void end();

}
