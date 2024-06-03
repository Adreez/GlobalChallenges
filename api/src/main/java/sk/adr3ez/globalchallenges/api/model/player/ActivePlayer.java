package sk.adr3ez.globalchallenges.api.model.player;

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
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.api.util.TimeUtils;

import java.util.UUID;

public final class ActivePlayer {


    private final GlobalChallenges plugin = GlobalChallengesProvider.get();
    private final ActiveChallenge activeChallenge;

    @Getter
    @Setter
    private Double score = 0D;

    @Getter
    @Setter
    private BossBar bossBar;

    @Getter
    private final UUID uuid;

    @Getter
    private final String name;

    @Getter
    private final Audience audience;

    @Getter
    @Setter
    private DBPlayerData dbPlayerData;

    @Getter
    @Setter
    private Long finishTime = 0L;

    @Getter
    @Setter
    private boolean finished = false;

    public ActivePlayer(UUID uuid, String name, Audience audience, DBPlayerData dbPlayerData, ActiveChallenge activeChallenge) {
        this.uuid = uuid;
        this.name = name;
        this.audience = audience;
        this.dbPlayerData = dbPlayerData;
        this.activeChallenge = activeChallenge;

        bossBar = BossBar.bossBar(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()),
                        Placeholder.parsed("score", String.valueOf(score)),
                        Placeholder.parsed("needed", String.valueOf(activeChallenge.getRequiredScore())),
                        Placeholder.component("time_left", Component.text(TimeUtils.formatMillis(activeChallenge.getTimeLeft() * 1000)))),
                0f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
        this.audience.showBossBar(bossBar);
    }

    public void addScore(@NotNull Double value) {
        this.score += value;
        audience.playSound(Sound.sound(Key.key("block.note_block.bell"), Sound.Source.MUSIC, 0.4f, 1f));

        //Return so game will be running until the challenge is ended
        if (activeChallenge.getRequiredScore() == -1)
            return;

        if (score < activeChallenge.getRequiredScore())
            return;

        if (!finished) {
            finished = true;
            finishTime = System.currentTimeMillis();
            bossBar.color(BossBar.Color.GREEN);
            bossBar.progress(1F);
            activeChallenge.finishPlayer(uuid);
        }
    }

    public void updateBossbar() {
        if (finished()) {
            bossBar.name(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_FINISHED_BOSSBAR.getRoute()),
                    Placeholder.component("time_left", Component.text(TimeUtils.formatMillis(activeChallenge.getTimeLeft() * 1000))),
                    Placeholder.component("finished_time", Component.text(TimeUtils.formatMillis(finishTime - activeChallenge.getStartTime())))
            ));
        } else {
            float status = (float) (score / activeChallenge.getRequiredScore());
            if (status > 1 || status < 0)
                status = 1;
            bossBar.name(MiniMessage.miniMessage().deserialize(plugin.getConfiguration().getString(ConfigRoutes.PLAYER_ACTIVE_BOSSBAR.getRoute()),
                    Placeholder.parsed("score", String.valueOf(score)),
                    Placeholder.parsed("needed", String.valueOf(activeChallenge.getRequiredScore())),
                    Placeholder.component("time_left", Component.text(TimeUtils.formatMillis(activeChallenge.getTimeLeft() * 1000)))));
            bossBar.progress(status);
        }
    }

    public boolean finished() {
        return finished;
    }
}
