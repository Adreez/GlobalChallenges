package sk.adr3ez.globalchallenges.api.model.challenge;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.model.GameManager;

import java.util.UUID;

public abstract class Challenge implements Listener {

    @NotNull
    protected GameManager gameManager;

    public Challenge(@NotNull GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Unique key for any challenge.
     * <b><Cannot/b> be the same for more than one challenge!
     *
     * @return string
     */
    @NotNull
    public abstract String getKey();

    /**
     * If specific challenge requires some dependency to work, you can make sure
     * that it is possible to load this challenge.
     * <p>
     * You can also hard-code this if you depend only on bukkit events, etc.
     *
     * @return boolean
     */
    @ApiStatus.Internal
    public abstract boolean canLoad();

    @NotNull
    public abstract Double getRequiredScore();

    public boolean canBeUnlimited() {
        return gameManager.getChallengesFile().getBoolean("challenges." + getKey() + ".unlimited_score");
    }

    @NotNull
    public String getName() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".name");
    }

    @NotNull
    public String getDescription() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".description");
    }

    /**
     * Check if challenge is enabled in configuration by server owner.
     *
     * @return boolean
     */
    public boolean isEnabled() {
        return gameManager.getChallengesFile().getBoolean("challenges." + getKey() + ".enabled");
    }

    @ApiStatus.Internal
    public boolean handleStart() {
        Bukkit.getPluginManager().registerEvents(this, GlobalChallengesProvider.get().getJavaPlugin());

        return this.onChallengeStart();
    }

    @ApiStatus.Internal
    public void handleEnd() {
        HandlerList.unregisterAll(this);

        this.onChallengeEnd();
    }

    protected void addScore(UUID uuid, Double value) {
        gameManager.getActiveChallenge().ifPresent(activeChallenge -> activeChallenge.getPlayer(uuid).ifPresent(player -> player.addScore(value)));
    }

    protected Double getScore(UUID uuid) {
        if (gameManager.getActiveChallenge().isPresent() && gameManager.getActiveChallenge().get().isJoined(uuid))
            return gameManager.getActiveChallenge().get().getPlayer(uuid).get().getScore();
        return 0D;
    }

    protected void setScore(UUID uuid, Double value) {
        if (gameManager.getActiveChallenge().isPresent() && gameManager.getActiveChallenge().get().isJoined(uuid))
            gameManager.getActiveChallenge().get().getPlayer(uuid).get().setScore(value);
    }

    protected boolean onChallengeStart() {
        return true;
    }

    protected void onChallengeEnd() {
    }


}
