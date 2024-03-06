package sk.adr3ez.globalchallenges.core.challenges;


import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.ArrayList;

public class HarvestCropsChallenge extends Challenge {

    private ArrayList<Integer> blocks;

    public HarvestCropsChallenge(@NotNull GameManager gameManager) {
        super(gameManager);
    }

    @NotNull
    @Override
    public String getKey() {
        return "harvest_crops";
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean onChallengeStart() {
        blocks = new ArrayList<>();
        return true;
    }

    @Override
    public void onChallengeEnd() {
        blocks = null;
    }

    @EventHandler
    public void onEvent(PlayerHarvestBlockEvent event) {
        if (event.getHarvestedBlock().getType() != Material.WHEAT)
            return;

        Ageable ageable = (Ageable) event.getHarvestedBlock().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge())
            return;

        addScore(event.getPlayer().getUniqueId(), 1D);
        blocks.add(event.getHarvestedBlock().getLocation().hashCode());
        event.getPlayer().sendMessage(String.valueOf(getScore(event.getPlayer().getUniqueId())));

    }

}
