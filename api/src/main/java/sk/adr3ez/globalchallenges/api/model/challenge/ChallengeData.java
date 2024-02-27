package sk.adr3ez.globalchallenges.api.model.challenge;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.model.Scoreable;

import java.util.HashMap;
import java.util.UUID;

public class ChallengeData implements Scoreable<Double> {

    private final ActiveChallenge activeChallenge;

    protected HashMap<UUID, Double> scores = new HashMap<>();

    public ChallengeData(ActiveChallenge activeChallenge) {
        this.activeChallenge = activeChallenge;
    }
    
    public void addScore(@NotNull Double value, @NotNull UUID uuid) {
        if (scores.containsKey(uuid)) {
            Double val = scores.get(uuid);
            scores.put(uuid, val + value);
        } else {
            scores.put(uuid, value);
        }
    }

    public void setScore(@NotNull Double value, @NotNull UUID uuid) {
        scores.put(uuid, value);
    }

    public void removeScore(@NotNull Double value, @NotNull UUID uuid) {
        if (scores.containsKey(uuid)) {
            Double val = scores.get(uuid);
            val -= value;
            scores.put(uuid, val);
        } else {
            scores.put(uuid, value);
        }
    }

    @Nullable
    public Double getScore(@NotNull UUID uuid) {
        return scores.get(uuid);
    }


}
