package sk.adr3ez.globalchallenges.core.challenges;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.Challenge;
import sk.adr3ez.globalchallenges.api.model.GameManager;

import java.util.UUID;

public class MineBlockChallenge extends Challenge<Double> implements Listener {
    public MineBlockChallenge(@NotNull YamlDocument document, @NotNull GameManager manager) {
        super(document);
    }

    @NotNull
    @Override
    public String getKey() {
        return "mine_block";
    }

    @NotNull
    @Override
    public String getName() {
        return document.getString("Challenges." + getKey() + ".name");
    }

    @NotNull
    @Override
    public String getDescription() {
        return document.getString("Challenges." + getKey() + ".description");
    }

    @Override
    public boolean isEnabled() {
        return document.getBoolean("Challenges." + getKey() + ".enabled");
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, GlobalChallengesProvider.get().getJavaPlugin());

        
    }

    @Override
    public void end() {

        HandlerList.unregisterAll(this);

    }

    @Override
    public void addScore(@NotNull Double value, @NotNull UUID target) {

    }

    @Override
    public void setScore(@NotNull Double value, @NotNull UUID target) {

    }

    @Nullable
    @Override
    public Double getScore(@NotNull UUID target) {
        return null;
    }
}
