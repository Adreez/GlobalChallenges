package sk.adr3ez.globalchallenges.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * This is provider class of GlobalChallenges plugin
 */
public final class GlobalChallengesProvider {


    @Nullable
    private static GlobalChallenges globalChallenges;

    private GlobalChallengesProvider() {
        throw new RuntimeException("This class cannot be utilized!");
    }


    public static GlobalChallenges get() {
        if (globalChallenges == null)
            throw new UnsupportedOperationException("GlobalChallenges plugin is not loaded yet!");
        return globalChallenges;
    }

    /**
     * Set the plugin instance, should be only used internally
     *
     * @param plugin instance
     */
    @ApiStatus.Internal
    public static void set(GlobalChallenges plugin) {
        globalChallenges = plugin;
    }

}
