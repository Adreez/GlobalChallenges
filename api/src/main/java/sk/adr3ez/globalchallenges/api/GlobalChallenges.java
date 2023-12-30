package sk.adr3ez.globalchallenges.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;

import java.util.Collection;

public interface GlobalChallenges {

    @NotNull YamlDocument getConfiguration();
    @NotNull PluginLogger getPluginLogger();

    //MessageManager - languages

    void broadcast(@NotNull Component component);

    @NotNull String getDataDirectory();

    @NotNull Collection<?> getOnlinePlayers();

    //Server version
    //getChallgeneManager - getActiveChallenge

}
