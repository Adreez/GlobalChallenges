package sk.adr3ez.globalchallenges.core.challenges;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MineBlockChallenge extends Challenge {

    private Material material;

    private boolean playerPlaced = true;


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
    public String getDescription() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".description")
                .replaceAll("%block%", material.name());
    }

    @Override
    public boolean onChallengeStart() {
        //Choose random block
        List<String> blocks = gameManager.getChallengesFile().getStringList("challenges." + getKey() + ".list");

        if (blocks.isEmpty())
            return false;

        blocks.removeIf(block -> Material.getMaterial(block.toUpperCase()) == null);

        Random random = new Random();
        int i = random.nextInt(blocks.size());

        material = Material.getMaterial(blocks.get(i).toUpperCase());

        playerPlaced = gameManager.getChallengesFile().getBoolean("challenges." + getKey() + ".block_player_placed");

        return material != null;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() != material)
            return;

        UUID uuid = event.getPlayer().getUniqueId();

        if (!gameManager.getActiveChallenge().get().isJoined(uuid))
            return;

        if (!playerPlaced && GlobalChallengesProvider.get().getPluginSettings().monitorBlocks()) {
            Location location = event.getBlock().getLocation();
            int loc = location.hashCode();
            if (location.getChunk().getPersistentDataContainer().has(new NamespacedKey(GlobalChallengesProvider.get().getJavaPlugin(), Integer.toString(loc, 16))))
                return;
        }

        addScore(uuid, 1D);
        GlobalChallengesProvider.get().getPluginLogger().warn("Total: " + gameManager.getActiveChallenge().get().getChallengeData().getScore(uuid));
    }
}
