package sk.adr3ez.globalchallenges.api.model;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class ChallengeScore<T extends Number> {

    private final @Nullable HashMap<UUID, T> values;

    public ChallengeScore() {
         values = new HashMap<>();
    }



}
