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

    /**
     * Get required score of the challenge which player have to do.
     *
     * @return Double
     */
    @NotNull
    public abstract Double getRequiredScore();

    /**
     * If challenge is unlimited, it means that there is no max value player
     * can get during challenge. The required value is set to -1 and player
     * with the highest value will win the challenge.
     *
     * @return boolean
     */
    public boolean canBeUnlimited() {
        return gameManager.getChallengesFile().getBoolean("challenges." + getKey() + ".unlimited_score");
    }

    /**
     * Get raw name of the challenge
     *
     * @return String
     */
    @NotNull
    public String getName() {
        return gameManager.getChallengesFile().getString("challenges." + getKey() + ".name");
    }

    /**
     * Get raw description of challenge.
     *
     * @return String
     */
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

    /**
     * This is internal method to initialize challenge
     *
     * @return boolean if successful
     */
    @ApiStatus.Internal
    public boolean handleStart() {
        Bukkit.getPluginManager().registerEvents(this, GlobalChallengesProvider.get().getJavaPlugin());

        return this.onChallengeStart();
    }

    /**
     * This is internal method to handle end of a challenge
     */
    @ApiStatus.Internal
    public void handleEnd() {
        HandlerList.unregisterAll(this);

        this.onChallengeEnd();
    }

    /**
     * This method will add specific amount of value to the active player account
     *
     * @param uuid  player unique id
     * @param value how much value do you want to add
     */
    protected void addScore(UUID uuid, Double value) {
        gameManager.getActiveChallenge().ifPresent(activeChallenge -> activeChallenge.getPlayer(uuid).ifPresent(player -> player.addScore(value)));
    }

    /**
     * Get the score of any player
     *
     * @param uuid unique id of player
     * @return Double value
     */
    protected Double getScore(UUID uuid) {
        if (gameManager.getActiveChallenge().isPresent() && gameManager.getActiveChallenge().get().isJoined(uuid))
            return gameManager.getActiveChallenge().get().getPlayer(uuid).get().getScore();
        return 0D;
    }

    /**
     * Set the score to any player that is online and is playing game
     *
     * @param uuid  unique id of player
     * @param value value you wish to set
     */
    protected void setScore(UUID uuid, Double value) {
        if (gameManager.getActiveChallenge().isPresent() && gameManager.getActiveChallenge().get().isJoined(uuid))
            gameManager.getActiveChallenge().get().getPlayer(uuid).get().setScore(value);
    }

    /**
     * You can override this method to make your own pre-start checks
     * if your challenge can be started. Useful if your challenge requires
     * dependencies.
     *
     * @return boolean if successful
     */
    protected boolean onChallengeStart() {
        return true;
    }

    /**
     * You can override this method to handle your end-challenge calls.
     */
    protected void onChallengeEnd() {
    }


}
