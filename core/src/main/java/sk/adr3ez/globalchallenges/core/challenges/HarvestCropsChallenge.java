package sk.adr3ez.globalchallenges.core.challenges;


import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HarvestCropsChallenge extends Challenge {

    private ArrayList<Integer> blocks;

    private Material material;

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

    @NotNull
    @Override
    public String getDescription() {
        return super.getDescription().replaceAll("%value%", material.toString());
    }

    @Override
    public Double getRequiredScore() {
        return -1D;
    }

    @Override
    public boolean onChallengeStart() {
        blocks = new ArrayList<>();

        List<String> materialList = new ArrayList<>(gameManager.getChallengesFile().getStringList("challenges." + getKey() + ".list"));

        if (materialList.isEmpty()) {
            return false;
        }

        int random = new Random().nextInt(materialList.size());
        while (Material.valueOf(materialList.get(random)) == null || PlantAges.valueOf(materialList.get(random)) == null) {
            materialList.remove(random);
            random = new Random().nextInt(materialList.size());
        }

        material = Material.valueOf(materialList.get(random));

        return material != null && PlantAges.valueOf(material.toString()) != null;
    }

    @Override
    public void onChallengeEnd() {
        blocks = null;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {

        if (event.getBlock().getType() != material)
            return;

        PlantAges plantAge = PlantAges.valueOf(material.toString());
        if (plantAge == null)
            return;

        int blockHash = event.getBlock().getLocation().hashCode();
        if (blocks.contains(blockHash))
            return;


        if (event.getBlock().getBlockData() instanceof Ageable ageable)
            if (ageable.getAge() < plantAge.getMaximumAge())
                return;

        if (blocks.contains(blockHash))
            blocks.remove(blockHash);

        addScore(event.getPlayer().getUniqueId(), 1D);

    }

    @EventHandler
    public void blockHarvest(PlayerHarvestBlockEvent event) {
        if (event.getHarvestedBlock().getType() != material)
            return;

        if (blocks.contains(event.getHarvestedBlock().getLocation().hashCode()))
            return;

        addScore(event.getPlayer().getUniqueId(), 1D);

    }

    @EventHandler
    public void blockBoneMealEvent(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BONE_MEAL)
            blocks.add(event.getClickedBlock().getLocation().hashCode());
    }

    /* Commented out because it blocks all farming
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        int blockHash = event.getBlock().getLocation().hashCode();

        if (event.getBlock().getType() == material)
            blocks.add(blockHash);
    }*/

    @Getter
    public enum PlantAges {

        // 0 = Can be broken but cannot be placed by player
        // -1 = can be harvested multiple times and also destroyed
        WHEAT(7),
        BEETROOTS(3),
        CARROTS(7),
        POTATOES(7),
        PUMPKIN(0),
        MELON(0),
        CACTUS(0),
        COCOA(2),
        SUGAR_CANE(0),
        SEA_PICKLE(0),
        NETHER_WART(3),
        SWEET_BERRY_BUSH(-1), // MAX 2 - Has also 3rd age with even more crops
        CAVE_VINES_PLANT(-1),
        PITCHER_CROP(4);

        private final int maximumAge;

        PlantAges(int maximumAge) {
            this.maximumAge = maximumAge;
        }

    }

}
