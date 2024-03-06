package sk.adr3ez.globalchallenges.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.api.util.log.PluginSettings;

import java.util.Collection;

public interface GlobalChallenges {

    @NotNull
    @Inject
    JavaPlugin getJavaPlugin();

    /**
     * Default config.yml
     *
     * @return YamlDocument - BoostedYaml
     */
    @NotNull
    @Inject
    @Named("config")
    YamlDocument getConfiguration();

    /**
     * Default methods that will be consistant for all versions of plugin.
     *
     * @return PluginLogger
     */
    @NotNull
    @Inject
    PluginLogger getPluginLogger();

    /**
     * All settings of the plugin are located in PluginSettings
     *
     * @return PluginSettings
     */
    @NotNull
    @Inject
    PluginSettings getPluginSettings();

    /**
     * Any data plugin stores into database can be obtained through DataManager class
     *
     * @return DataManager
     */
    @NotNull
    @Inject
    DataManager getDataManager();

    @NotNull
    @Inject
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
