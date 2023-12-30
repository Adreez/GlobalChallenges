package sk.adr3ez.globalchallenges.api.data;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Storage {

    boolean createUser(@NotNull UUID uuid);
    boolean exists(@NotNull UUID uuid);
    boolean deleteUser(@NotNull UUID uuid);
    boolean addJoin(@NotNull UUID uuid);
    boolean addWin(@NotNull UUID uuid);

}
