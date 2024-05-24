package sk.adr3ez.globalchallenges.core.challenges;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayersKilledChallenge extends Challenge {

    private HashMap<UUID, Collection<UUID>> players;

    public PlayersKilledChallenge(@NotNull GameManager gameManager) {
        super(gameManager);
    }

    @NotNull
    @Override
    public String getKey() {
        return "players_killed";
    }

    @Override
    public boolean canLoad() {
        return false;
    }

    @Override
    public Double getRequiredScore() {
        return 0D;
    }

    @Override
    public boolean onChallengeStart() {
        players = new HashMap<>();
        return true;
    }

    @Override
    public void onChallengeEnd() {
        players = null;
    }

    public void onEvent(PlayerDeathEvent event) {
        UUID killer = event.getEntity().getKiller().getUniqueId();
        if (!gameManager.getActiveChallenge().get().isJoined(killer))
            return;
        UUID entity = event.getEntity().getUniqueId();

        if (!players.containsKey(killer))
            players.put(killer, List.of(entity));
        else
            players.get(killer).add(entity);

        //Disable killing same player all over again
        if (players.get(killer).contains(entity))
            return;

        addScore(killer, 1D);
    }
}
