package sk.adr3ez.globalchallenges.core.challenges;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.Challenge;
import sk.adr3ez.globalchallenges.api.model.GameManager;

import java.util.UUID;

public class MineBlockChallenge extends Challenge<Integer> implements Listener {

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

    @NotNull
    @Override
    public String getName() {
        return gameManager.getChallengesFile().getString("Challenges." + getKey() + ".name");
    }

    @NotNull
    @Override
    public String getDescription() {
        return gameManager.getChallengesFile().getString("Challenges." + getKey() + ".description");
    }

    @Override
    public boolean isEnabled() {
        return gameManager.getChallengesFile().getBoolean("Challenges." + getKey() + ".enabled");
    }

    @Override
    public void onStart() {
        Bukkit.getPluginManager().registerEvents(this, GlobalChallengesProvider.get().getJavaPlugin());
    }

    @Override
    public void onEnd() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void addScore(@NotNull Integer value, @NotNull UUID target) {

    }

    @Override
    public void setScore(@NotNull Integer value, @NotNull UUID target) {

    }

    @Nullable
    @Override
    public Integer getScore(@NotNull UUID target) {
        return null;
    }
}
