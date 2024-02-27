package sk.adr3ez.globalchallenges.core.challenges;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MineBlockChallenge extends Challenge {

    private Material material;

    public MineBlockChallenge(@NotNull GameManager gameManager) {
        super(gameManager);
    }


    @NotNull
    @Override
    public String getKey() {
        return "mine_block";
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public boolean onChallengeStart() {
        //Choose random block
        List<String> blocks = gameManager.getChallengesFile().getStringList("challenges." + getKey() + ".list");

        if (blocks.isEmpty())
            return false;

        blocks.removeIf(block -> Material.getMaterial(block.toUpperCase()) == null);

        material = Material.getMaterial(blocks.get(new Random(blocks.size()).nextInt()).toUpperCase());

        return material != null;
    }


    @Override
    public void onChallengeEnd() {
    }

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() == material) {
            UUID uuid = event.getPlayer().getUniqueId();
            if (gameManager.getActiveChallenge().isPresent() && gameManager.getActiveChallenge().get().isJoined(uuid)) {
                addScore(uuid, 1D);
            }
        }
    }
}
