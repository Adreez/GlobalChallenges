package sk.adr3ez.globalchallenges.core.challenges;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.ChallengeType;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MineBlockChallenge extends Challenge {

    private ChallengeMaterial challengeMaterial;

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
    public List<ChallengeType> getTypes() {
        return List.of(ChallengeType.values());
    }

    @Override
    public Double getRequiredScore() {
        return (double) (new Random().nextInt(challengeMaterial.maximum - challengeMaterial.minimum) + 1);
    }

    @Override
    public String getDescription() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".description")
                .replaceAll("%block%", challengeMaterial.getMaterial().name());
    }

    @Override
    public boolean onChallengeStart() {
        //Choose random block
        List<ChallengeMaterial> blocks = new ArrayList<>();

        gameManager.getChallengesFile().getStringList("challenges." + getKey() + ".list").forEach(value -> {
            ChallengeMaterial challengeMat = new ChallengeMaterial(value);
            if (challengeMat.getMaterial() == null)
                return;
            blocks.add(challengeMat);
        });

        blocks.removeIf(block -> block.getMaterial() == null);
        if (blocks.isEmpty())
            return false;


        Random random = new Random();
        int i = random.nextInt(blocks.size());
        challengeMaterial = blocks.get(i);


        playerPlaced = gameManager.getChallengesFile().getBoolean("challenges." + getKey() + ".block_player_placed");

        return challengeMaterial != null;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(BlockBreakEvent event) {
        if (event.getBlock().getType() != challengeMaterial.getMaterial())
            return;

        UUID uuid = event.getPlayer().getUniqueId();

        if (!gameManager.getActiveChallenge().get().isJoined(uuid))
            return;

        if (!playerPlaced && GlobalChallengesProvider.get().getConfiguration().getBoolean(ConfigRoutes.SETTINGS_MONITOR_BLOCKS.getRoute())) {
            Location location = event.getBlock().getLocation();
            int loc = location.hashCode();
            if (location.getChunk().getPersistentDataContainer().has(new NamespacedKey(GlobalChallengesProvider.get().getJavaPlugin(), Integer.toString(loc, 16))))
                return;
        }

        addScore(uuid, 1D);
    }

    @Getter
    public static class ChallengeMaterial {

        @Nullable
        private final Material material;

        @Nullable
        private Integer maximum = 64;

        @Nullable
        private Integer minimum = 1;

        public ChallengeMaterial(String materialString) {
            String[] strings = materialString.split(":");

            this.material = Material.getMaterial(strings[0].toUpperCase());
            if (isInt(strings[1]))
                if (Integer.parseInt(strings[1]) > 0)
                    this.maximum = Integer.valueOf(strings[1]);
            if (isInt(strings[2]))
                if (Integer.parseInt(strings[2]) > 0)
                    this.minimum = Integer.valueOf(strings[2]);
        }

        private boolean isInt(@Nullable String s) {
            if (s == null)
                return false;
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }

    }
}
