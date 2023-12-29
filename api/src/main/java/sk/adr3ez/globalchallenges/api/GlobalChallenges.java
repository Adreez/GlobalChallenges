package sk.adr3ez.globalchallenges.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;

public interface GlobalChallenges {

    @NotNull YamlDocument getConfig();
    @NotNull PluginLogger getLogger();

    //MessageManager - languages

    void broadcast(@NotNull Component component);

    //getChallgeneManager - getActiveChallenge

}
