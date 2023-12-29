package sk.adr3ez.globalchallenges.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Challenge<T extends Number> {

    @NotNull String getName();
    @NotNull String getDescription();
    boolean enable();
    boolean disable();

    void addScore(@NotNull T value, @NotNull UUID target);
    void setScore(@NotNull T value, @NotNull UUID target);
    @Nullable
    T getScore(@NotNull UUID target);

}
