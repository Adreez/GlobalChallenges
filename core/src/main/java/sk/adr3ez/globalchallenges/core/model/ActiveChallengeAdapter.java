package sk.adr3ez.globalchallenges.core.model;

import org.bukkit.entity.Player;
import sk.adr3ez.globalchallenges.api.model.ActiveChallenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ActiveChallengeAdapter implements ActiveChallenge {

    private final List<UUID> players = new ArrayList<>();

    @Override
    public List<UUID> getJoinedPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Optional<UUID> getPlayer(UUID uuid) {
        if (players.contains(uuid))
            return Optional.of(players.get(players.indexOf(uuid)));
        else
            return Optional.empty();
    }

    @Override
    public boolean add(UUID uuid) {
        return players.add(uuid);
    }

    @Override
    public int countJoinedPlayers(UUID uuid) {
        return players.size();
    }

    @Override
    public boolean isJoined(Player player) {
        return isJoined(player.getUniqueId());
    }

    @Override
    public boolean isJoined(UUID uuid) {
        return players.contains(uuid);
    }
}
