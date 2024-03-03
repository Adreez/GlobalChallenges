package sk.adr3ez.globalchallenges.api.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Scoreable<T extends Number> {

    void addScore(@NotNull T value, @NotNull UUID target);

    void setScore(@NotNull T value, @NotNull UUID target);

    void removeScore(@NotNull T value, @NotNull UUID target);

    @Nullable
    T getScore(@NotNull UUID target);

}
