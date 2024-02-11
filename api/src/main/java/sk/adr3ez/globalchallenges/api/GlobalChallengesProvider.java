package sk.adr3ez.globalchallenges.api;

import org.jetbrains.annotations.Nullable;

public class GlobalChallengesProvider {


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

    public static void set(GlobalChallenges plugin) {
        globalChallenges = plugin;
    }

}
