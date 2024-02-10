package sk.adr3ez.globalchallenges.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.api.util.log.PluginSettings;

import java.util.Collection;

public interface GlobalChallenges {

    @NotNull
    JavaPlugin getGCPlugin();

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
     * All settings of the plugin are located in PluginSettings
     *
     * @return PluginSettings
     */
    @NotNull
    PluginSettings getPluginSettings();

    /**
     * Any data plugin stores into database can be obtained through DataManager class
     *
     * @return DataManager
     */
    @NotNull
    DataManager getDataManager();

    //MessageManager - languages

    void broadcast(@NotNull Component component);

    @NotNull
    String getDataDirectory();

    @NotNull
    Collection<?> getOnlinePlayers();

    //Server version
    //getChallgeneManager - getActiveChallenge

}
