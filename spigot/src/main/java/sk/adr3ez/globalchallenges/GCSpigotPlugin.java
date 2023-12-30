package sk.adr3ez.globalchallenges;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.util.SpigotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public final class GCSpigotPlugin extends JavaPlugin implements GlobalChallenges {

    @Nullable
    private BukkitAudiences adventure;
    private @Nullable YamlDocument configurationFile;

    //https://docs.advntr.dev/platform/bukkit.html
    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);

        try {
            configurationFile = YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException e) {
            getLogger().warning("There was error with loading config.yml, please try to reload the plugin \n" + e);
        }
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    @Override
    public @NotNull YamlDocument getConfiguration() {
        if (configurationFile == null)
            throw new IllegalStateException("You are trying to access configuration while plugin is not loaded!");
        return configurationFile;
    }

    @NotNull
    @Override
    public PluginLogger getPluginLogger() {
        return new SpigotLogger();
    }

    @Override
    public void broadcast(@NotNull Component component) {
        for (Player player : getOnlinePlayers()) {
            adventure().player(player).sendMessage(component);
        }
    }

    @Override
    public @NotNull String getDataDirectory() {
        return getDataFolder().getAbsolutePath();
    }

    @Override
    public @NotNull Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }
}
