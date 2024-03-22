package sk.adr3ez.globalchallenges.api.model.player;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class ChallengePlayer {

    private Double score = 0D;
    private BossBar bossBar;

    private UUID uuid;


    public void addScore(@NotNull Double value) {
        this.score += value;
    }

    public void removeScore(@NotNull Double value) {
        this.score -= value;
    }


}
