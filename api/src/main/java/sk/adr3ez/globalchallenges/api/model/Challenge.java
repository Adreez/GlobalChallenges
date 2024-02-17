package sk.adr3ez.globalchallenges.api.model;

import org.jetbrains.annotations.NotNull;

public abstract class Challenge<T extends Number> implements Scoreable<T> {

    @NotNull
    protected GameManager gameManager;

    public Challenge(@NotNull GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @NotNull
    public abstract String getKey();

    /**
     * If specific challenge requires
     *
     * @return
     */
    public abstract boolean canLoad();

    @NotNull
    public abstract String getName();

    @NotNull
    public abstract String getDescription();

    public abstract boolean isEnabled();

    public abstract void onStart();

    public abstract void onEnd();

}
