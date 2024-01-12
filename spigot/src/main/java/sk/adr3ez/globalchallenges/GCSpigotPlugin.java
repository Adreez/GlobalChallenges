package sk.adr3ez.globalchallenges;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.api.util.log.PluginSettings;
import sk.adr3ez.globalchallenges.core.util.DataManagerAdapter;
import sk.adr3ez.globalchallenges.core.util.PluginSettingsAdapter;
import sk.adr3ez.globalchallenges.util.SpigotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public final class GCSpigotPlugin extends JavaPlugin implements GlobalChallenges {


    @Nullable
    private BukkitAudiences adventure;
    private @Nullable YamlDocument configurationFile;
    private @Nullable PluginSettings pluginSettings;

    private @Nullable DataManager dataManager;

    @Override
    public void onEnable() {
        long startupTime = System.currentTimeMillis();

        getPluginLogger().info(ConsoleColors.format("&y[&cGlobalChallenges&y] &gInitializing plugin...&reset"));

        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        this.adventure = BukkitAudiences.create(this);
        this.pluginSettings = new PluginSettingsAdapter(this);
        this.dataManager = new DataManagerAdapter(this);

        try {
            configurationFile = YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException e) {
            getPluginLogger().warn("There was error with loading config.yml, please try to reload the plugin \n" + e);
        }

        setupCommands();
        getPluginLogger().info(ConsoleColors.format("&y[&cGlobalChallenges&y] &gPlugin has been loaded (" + (System.currentTimeMillis() - startupTime) + " ms)&reset"));
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        getDataManager().getFactory().close();
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
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

    @NotNull
    @Override
    public PluginSettings getPluginSettings() {
        if (pluginSettings == null)
            throw new IllegalStateException("Tried to access plugin settings while plugin is not loaded yet!");
        return pluginSettings;
    }

    @NotNull
    @Override
    public DataManager getDataManager() {
        if (dataManager == null)
            throw new IllegalStateException("Tried to access data manager while plugin is not loaded yet!");
        return dataManager;
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

    private void setupCommands() {
        new CommandAPICommand("globalchallenges")
                .withAliases("gch", "globalch", "glch")
                .withPermission("globalchallenges.use")
                .withSubcommand(new CommandAPICommand("start")
                        .withArguments(new GreedyStringArgument("challengeID")
                                .executes((sender, args) -> {
                                    if (args.get("challengeID") == "REPLACE") {
                                        sender.sendMessage("Yeey - replace");
                                    } else if (args.get("challengeID") == "YES") {
                                        sender.sendMessage("Yeeey - YES");
                                    }
                                    sender.sendMessage("NOPE!");
                                })))
                .executes((sender, args) -> {
                    sender.sendMessage("TEST");
                })
                .register();
    }

    public static class ConsoleColors {
        @NotNull
        public static final String RESET = "\u001B[0m";
        @NotNull
        public static final String BLACK = "\u001B[30m";
        @NotNull
        public static final String RED = "\u001B[31m";
        @NotNull
        public static final String GREEN = "\u001B[32m";
        @NotNull
        public static final String YELLOW = "\u001B[33m";
        @NotNull
        public static final String BLUE = "\u001B[34m";
        @NotNull
        public static final String PURPLE = "\u001B[35m";
        @NotNull
        public static final String CYAN = "\u001B[36m";
        @NotNull
        public static final String WHITE = "\u001B[37m";

        public static String format(String input) {
            return input.replaceAll("&reset", RESET)
                    .replaceAll("&0", BLACK)
                    .replaceAll("&r", RED)
                    .replaceAll("&g", GREEN)
                    .replaceAll("&y", YELLOW)
                    .replaceAll("&b", BLUE)
                    .replaceAll("&c", CYAN)
                    .replaceAll("&w", WHITE);

        }
    }
}
