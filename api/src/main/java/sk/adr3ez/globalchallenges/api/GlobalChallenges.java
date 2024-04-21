package sk.adr3ez.globalchallenges.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;

import java.util.Collection;

public interface GlobalChallenges {

    @NotNull
    JavaPlugin getJavaPlugin();

    /**
     * Default config.yml
     *
     * @return YamlDocument - BoostedYaml
     */
    @NotNull
    YamlDocument getConfiguration();

    /**
     * Default methods that will be consistant for all versions of plugin.
     *
     * @return PluginLogger
     */
    @NotNull
    PluginLogger getPluginLogger();

    /**
     * Any data plugin stores into database can be obtained through DataManager class
     *
     * @return DataManager
     */
    //@NotNull
    //DataManager getDataManager();
    @NotNull
    GameManager getGameManager();

    //MessageManager - languages

    void broadcast(@NotNull Component component);

    void broadcastTitle(@NotNull Title title);

    @NotNull
    String getDataDirectory();

    @NotNull
    Collection<?> getOnlinePlayers();

    //Server version

}
