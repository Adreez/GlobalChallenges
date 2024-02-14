package sk.adr3ez.globalchallenges.api.model;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActiveChallenge {

    List<UUID> getJoinedPlayers();

    Optional<UUID> getPlayer(UUID uuid);

    boolean add(UUID uuid);

    int countJoinedPlayers(UUID uuid);

    boolean isJoined(Player player);

    boolean isJoined(UUID uuid);
}
