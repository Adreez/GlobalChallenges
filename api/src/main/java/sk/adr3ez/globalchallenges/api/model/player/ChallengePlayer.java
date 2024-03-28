package sk.adr3ez.globalchallenges.api.model.player;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.util.UUID;

@Getter
@Setter
public class ChallengePlayer {

    private Double score = 0D;
    private BossBar bossBar;

    private UUID uuid;

    private Audience audience;

    public ChallengePlayer(UUID uuid, Audience audience) {
        this.uuid = uuid;
        this.audience = audience;

        //Show bossbar
        bossBar = BossBar.bossBar(MiniMessage.miniMessage().deserialize(GlobalChallengesProvider.get().getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()))
                , 0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        audience.showBossBar(bossBar);
    }


    public void addScore(@NotNull Double value) {
        this.score += value;
    }

    public void removeScore(@NotNull Double value) {
        this.score -= value;
    }

    public void updateBossbar() {
        float status = (float) (score / GlobalChallengesProvider.get().getGameManager().getActiveChallenge().get().getRequiredScore());
        if (status > 1)
            status = 1;
        bossBar.name(MiniMessage.miniMessage().deserialize(GlobalChallengesProvider.get().getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()),
                Placeholder.parsed("score", String.valueOf(score)),
                Placeholder.component("time_left", Component.text("11:58"))));
        bossBar.progress(status);
    }


}
