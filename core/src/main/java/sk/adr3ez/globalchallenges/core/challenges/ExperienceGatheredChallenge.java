package sk.adr3ez.globalchallenges.core.challenges;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

public class ExperienceGatheredChallenge extends Challenge {
    public ExperienceGatheredChallenge(@NotNull GameManager gameManager) {
        super(gameManager);
    }

    @NotNull
    @Override
    public String getKey() {
        return "xp_gathered";
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public String getDescription() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".description")
                .replaceAll("%value%", String.valueOf(getRequiredScore()));
    }

    @Override
    public Double getRequiredScore() {
        return 250D;
    }

    @EventHandler
    public void onEvent(PlayerExpChangeEvent event) {
        addScore(event.getPlayer().getUniqueId(), (double) event.getAmount());
    }

}
