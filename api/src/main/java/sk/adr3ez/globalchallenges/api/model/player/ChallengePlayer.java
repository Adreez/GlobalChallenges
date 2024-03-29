package sk.adr3ez.globalchallenges.api.model.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;

import java.util.UUID;

@Getter
@Setter
public class ChallengePlayer {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private GlobalChallenges plugin = GlobalChallengesProvider.get();

    private Double score = 0D;
    private BossBar bossBar;

    private UUID uuid;

    private Audience audience;

    @Setter(AccessLevel.NONE)
    private final Double requiredScore = plugin.getGameManager().getActiveChallenge().get().getRequiredScore();

    private Long finishTime = 0L;

    public ChallengePlayer(UUID uuid, Audience audience) {
        this.uuid = uuid;
        this.audience = audience;

        //Show bossbar
        bossBar = BossBar.bossBar(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()))
                , 0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        audience.showBossBar(bossBar);
    }


    public void addScore(@NotNull Double value) {
        this.score += value;
        audience.playSound(Sound.sound(Key.key("block.note_block.bell"), Sound.Source.MUSIC, 0.4f, 1f));

        if (score < requiredScore)
            return;

        finishTime = System.currentTimeMillis();
        bossBar.color(BossBar.Color.GREEN);
        bossBar.progress(1F);
    }

    public void removeScore(@NotNull Double value) {
        this.score -= value;
    }

    public void updateBossbar() {
        if (finished()) {
            bossBar.name(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_FINISHED_BOSSBAR.getRoute()),
                    Placeholder.component("time_left", Component.text(plugin.getGameManager().getActiveChallenge().get().getTimeLeft())),
                    Placeholder.component("finished_time", Component.text(finishTime - plugin.getGameManager().getActiveChallenge().get().getStartTime()))
            ));
        } else {
            float status = (float) (score / requiredScore);
            if (status > 1)
                status = 1;
            bossBar.name(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()),
                    Placeholder.parsed("score", String.valueOf(score)),
                    Placeholder.parsed("needed", String.valueOf(requiredScore)),
                    Placeholder.component("time_left", Component.text(plugin.getGameManager().getActiveChallenge().get().getTimeLeft()))));
            bossBar.progress(status);
        }
    }

    public boolean finished() {
        return finishTime > 0;
    }


}
