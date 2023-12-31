package sk.adr3ez.globalchallenges.core.database.adapter;

import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.database.Storage;
import sk.adr3ez.globalchallenges.core.database.AbstractTable;
import sk.adr3ez.globalchallenges.core.database.ConnectionPoolManager;

import java.util.UUID;

public class MySQLStorageAdapter extends AbstractTable implements Storage {
    public MySQLStorageAdapter(@NotNull ConnectionPoolManager manager, @NotNull String table) {
        super(manager, table);
    }

    @Override
    public void createTable() {

    }

    @Override
    public boolean createUser(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean exists(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean deleteUser(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addJoin(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public boolean addWin(@NotNull UUID uuid) {
        return false;
    }

    @Override
    public int getWins(@NotNull UUID uuid) {
        return 0;
    }

    @Override
    public int getJoins(@NotNull UUID uuid) {
        return 0;
    }
}
