package sk.adr3ez.globalchallenges.spigot;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.DataManager;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.api.util.log.PluginSettings;
import sk.adr3ez.globalchallenges.core.database.DataManagerAdapter;
import sk.adr3ez.globalchallenges.core.model.GameManagerAdapter;
import sk.adr3ez.globalchallenges.core.util.PluginSettingsAdapter;
import sk.adr3ez.globalchallenges.spigot.util.BlockListener;
import sk.adr3ez.globalchallenges.spigot.util.SpigotLogger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class GCSpigotPlugin extends JavaPlugin implements GlobalChallenges {
    private @Nullable BukkitAudiences adventure;
    private @Nullable YamlDocument configurationFile;
    private @Nullable PluginSettings pluginSettings;

    private @Nullable DataManager dataManager;

    private @Nullable GameManager gameManager;

    @Override
    public void onEnable() {
        long startupTime = System.currentTimeMillis();

        GlobalChallengesProvider.set(this);

        getPluginLogger().info(ConsoleColors.format("&y[&cGlobalChallenges&y] &gInitializing plugin...&reset"));

        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        this.adventure = BukkitAudiences.create(this);
        this.pluginSettings = new PluginSettingsAdapter(this);
        this.dataManager = new DataManagerAdapter(this);
        this.gameManager = new GameManagerAdapter(this);

        try {
            configurationFile = YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException e) {
            getPluginLogger().warn("There was error with loading config.yml, please try to reload the plugin \n" + e);
        }

        if (pluginSettings.monitorBlocks())
            Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);

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

        CommandAPI.unregister("globalchallenges");
    }

    public @NotNull BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
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

    @NotNull
    @Override
    public GameManager getGameManager() {
        return gameManager;
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
                .withSubcommand(new CommandAPICommand("help")
                        .withPermission("globalchallenges.admin")
                        .executes((sender, args) -> {
                            adventure().sender(sender).sendMessage(MiniMessage.miniMessage().deserialize("""
                                    GlobalChallenges
                                    /glch help
                                    /glch game <action> <gameID>
                                    /glch list
                                    """));
                        })
                )
                .withSubcommand(new CommandAPICommand("list")
                        .withPermission("globalchallenges.admin")
                        .executes((sender, args) -> {
                            Objects.requireNonNull(gameManager).getLoadedChallenges().forEach(challenge ->
                                    sender.sendMessage("Loaded: " + challenge.getKey() + "/ (Class) " + challenge.getClass().getName()));
                        })
                )
                .withSubcommand(new CommandAPICommand("game")
                        .withPermission("globalchallenges.admin")
                        .withArguments(new StringArgument("action")
                                .setListed(true).replaceSuggestions(ArgumentSuggestions.strings("start", "end")))
                        .withOptionalArguments(new StringArgument("gameID")
                                .replaceSuggestions(ArgumentSuggestions.stringsAsync(info ->
                                        CompletableFuture.supplyAsync(() ->
                                                gameManager.getLoadedChallengesKeys().toArray(new String[0])))))
                        .executes((sender, args) -> {

                            String action = (String) args.get("action");

                            switch (action) {
                                case "start":
                                    if (gameManager.getActiveChallenge().isEmpty()) {
                                        if (args.getOptional("gameID").isPresent()) {
                                            //Start exact game

                                            Challenge challenge = gameManager.getChallenge(args.getOptional("gameID").get().toString());

                                            if (challenge != null) {
                                                sender.sendMessage("Start exact game: " + challenge.getKey());
                                                if (gameManager.start(challenge)) {
                                                    sender.sendMessage("Game started");
                                                } else {
                                                    sender.sendMessage("Game failed to start");
                                                }
                                            } else {
                                                sender.sendMessage("This game is not loaded!");
                                            }

                                        } else {
                                            //Start random one
                                            gameManager.startRandom();
                                            sender.sendMessage("Starting random game");
                                        }
                                    } else {
                                        sender.sendMessage("To start the challenge you have to end active one or wait until it will be done automaticaly.");
                                    }
                                    break;
                                case "end", "stop":
                                    //End a game if started
                                    if (gameManager.getActiveChallenge().isPresent()) {

                                        gameManager.endActive();

                                        sender.sendMessage("Stopping game (Sender)");
                                    } else {
                                        sender.sendMessage("There's no active game.");
                                    }
                                    break;
                                default:
                                    sender.sendMessage("This command does not exist!");
                                    break;
                            }
                        }))
                .withSubcommand(new CommandAPICommand("join")
                        .withPermission("globalchallenges.join")
                        .executesPlayer((sender, args) -> {
                            //Join cmd
                            if (gameManager.getActiveChallenge().isPresent()) {
                                sender.sendMessage("You've joined the game!");
                                gameManager.getActiveChallenge().get().joinPlayer(sender.getUniqueId());
                            } else {
                                sender.sendMessage("No active game to join!");
                            }
                        }))
                .executes((sender, args) -> {
                            Bukkit.getServer().dispatchCommand(sender, "glch help");
                        }
                )
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
