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
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.DatabaseManager;
import sk.adr3ez.globalchallenges.api.database.entity.DBGame;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayerData;
import sk.adr3ez.globalchallenges.api.model.GameManager;
import sk.adr3ez.globalchallenges.api.model.challenge.ActiveChallenge;
import sk.adr3ez.globalchallenges.api.model.challenge.Challenge;
import sk.adr3ez.globalchallenges.api.util.ConfigRoutes;
import sk.adr3ez.globalchallenges.api.util.TimeUtils;
import sk.adr3ez.globalchallenges.api.util.log.PluginLogger;
import sk.adr3ez.globalchallenges.core.database.DatabaseManagerImp;
import sk.adr3ez.globalchallenges.core.database.dao.GameDAO;
import sk.adr3ez.globalchallenges.core.model.GameManagerAdapter;
import sk.adr3ez.globalchallenges.spigot.util.BlockListener;
import sk.adr3ez.globalchallenges.spigot.util.SpigotLogger;
import sk.adr3ez.globalchallenges.spigot.util.UtilListener;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class GCSpigotPlugin extends JavaPlugin implements GlobalChallenges {
    private @Nullable BukkitAudiences adventure;
    private @Nullable YamlDocument configurationFile;

    private @Nullable GameManager gameManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        long startupTime = System.currentTimeMillis();

        getPluginLogger().info(ConsoleColors.format("&y[&cGlobalChallenges&y] &gInitializing plugin...&reset"));

        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        this.adventure = BukkitAudiences.create(this);

        try {
            configurationFile = YamlDocument.create(new File(getDataFolder(), "config.yml"), Objects.requireNonNull(getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder()
                            .setVersioning(new BasicVersioning("file-version")).build());
        } catch (IOException e) {
            getPluginLogger().warn("There was error with loading config.yml, please try to reload the plugin \n" + e);
        }

        this.databaseManager = new DatabaseManagerImp(this);

        this.gameManager = new GameManagerAdapter(this);

        Bukkit.getPluginManager().registerEvents(new UtilListener(), this); // Setup utility listener

        if (getConfiguration().getBoolean(ConfigRoutes.SETTINGS_MONITOR_BLOCKS.getRoute()))
            Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);

        setupCommands();
        getPluginLogger().info(ConsoleColors.format("&y[&cGlobalChallenges&y] &gPlugin has been loaded (" + (System.currentTimeMillis() - startupTime) + " ms)&reset"));
    }

    @Override
    public void onLoad() {
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        GlobalChallengesProvider.set(this);
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        this.databaseManager.close();
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
    public GameManager getGameManager() {
        if (gameManager == null)
            throw new IllegalStateException("Tried to access game manager while plugin is not loaded yet!");
        return gameManager;
    }

    @Override
    public @NotNull DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    @Override
    public void broadcast(@NotNull Component component) {
        for (Player player : getOnlinePlayers()) {
            adventure().player(player).sendMessage(component);
        }
    }

    @Override
    public void broadcastTitle(@NotNull Title title) {
        for (Player player : getOnlinePlayers()) {
            adventure().player(player).showTitle(title);
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
                            for (String s : configurationFile.getStringList(ConfigRoutes.MESSAGES_COMMANDS_HELP.getRoute())) {
                                adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(s));
                            }
                        })
                )
                /*.withSubcommand(new CommandAPICommand("list")
                        .withPermission("globalchallenges.admin")
                        .executes((sender, args) -> {
                            Objects.requireNonNull(gameManager).getLoadedChallenges().forEach(challenge ->
                                    sender.sendMessage("Loaded games: " + challenge.getKey() + "/ (Class) " + challenge.getClass().getName()));
                        })
                )*/
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
                                                if (gameManager.start(challenge)) {
                                                    adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_START_STARTED.getRoute())));
                                                } else {
                                                    adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(ConfigRoutes.MESSAGES_COMMANDS_START_FAILED.getRoute()));
                                                }
                                            } else {
                                                adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_START_NOTLOADED.getRoute())));
                                            }

                                        } else {
                                            //Start random one
                                            gameManager.startRandom();
                                            adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_START_RANDOM.getRoute())));
                                        }
                                    } else {
                                        adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_START_ALREADYSTARTED.getRoute())));
                                    }
                                    break;
                                case "end", "stop":
                                    //End a game if started
                                    if (gameManager.getActiveChallenge().isPresent()) {

                                        gameManager.endActive();

                                        adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_STOP_SUCCESSFUL.getRoute())));
                                    } else {
                                        adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_STOP_NOACTIVE.getRoute())));
                                    }
                                    break;
                                default:
                                    adventure.sender(sender).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_NOEXIST.getRoute())));
                                    break;
                            }
                        }))
                .withSubcommand(new CommandAPICommand("join")
                        .withPermission("globalchallenges.join")
                        .executesPlayer((player, args) -> {
                            //Join cmd
                            if (gameManager.getActiveChallenge().isPresent()) {

                                ActiveChallenge activeChallenge = gameManager.getActiveChallenge().get();

                                if (!activeChallenge.isJoined(player.getUniqueId())) {
                                    adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_JOIN_SUCCESSFUL.getRoute())));
                                    activeChallenge.joinPlayer(player.getUniqueId(), player.getName(), adventure().player(player));
                                } else {
                                    adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_JOIN_ALREADYJOINED.getRoute())));
                                }

                            } else {
                                adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_JOIN_FAILED.getRoute())));
                            }
                        }))
                .withSubcommand(new CommandAPICommand("results")
                        .withOptionalArguments(new LongArgument("gameID"))
                        .executesPlayer((player, args) -> {
                            Bukkit.getScheduler().runTaskAsynchronously(this, runnable -> {
                                DBGame game;
                                if (args.getOptional("gameID").isPresent()) {
                                    game = GameDAO.findById((Long) args.get("gameID"));
                                } else {
                                    game = GameDAO.getLast();
                                }
                                if (game == null) {
                                    adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_RESULTS_GAMENOTFOUND.getRoute())));
                                    return;
                                }
                                List<DBPlayerData> list = GameDAO.getPlayerData(game.getId());

                                if (list.isEmpty()) {
                                    adventure.player(player).sendMessage(MiniMessage.miniMessage().deserialize(configurationFile.getString(ConfigRoutes.MESSAGES_COMMANDS_RESULTS_NODATA.getRoute())));
                                    return;
                                }

                                list.sort(Comparator.comparingInt(DBPlayerData::getPosition));


                                ArrayList<DBPlayerData> notFinished = new ArrayList<>();
                                StringBuilder result = new StringBuilder();
                                for (DBPlayerData playerData : list) { //<- Line 300
                                    if (playerData.isFinished())
                                        result.append("<br><b><color:#245a6a>").append(playerData.getPosition() == 0 ? "N/A" : playerData.getPosition()).append(":</b>")
                                                .append("<color:#009c15> ").append(playerData.getPlayer().getNick())
                                                .append("\n<gray>  »").append(playerData.getTimeFinished() != null ?
                                                        TimeUtils.formatMillis(playerData.getTimeFinished().getTime() - playerData.getTimeJoined().getTime())
                                                        : "Not finished");
                                    else
                                        notFinished.add(playerData);
                                }
                                if (!notFinished.isEmpty()) {
                                    result.append("<br><black><b>Not finished:</b>");
                                    for (DBPlayerData playerData : notFinished) {
                                        result.append("<br><color:#009c15> ").append(playerData.getPlayer().getNick());
                                    }
                                }

                                adventure().player(player).openBook(Book.book(Component.text("Results"), Component.text("GlobalChallenges"),
                                        MiniMessage.miniMessage().deserialize("""
                                                 <b><color:#245a6a>Game id:</b> %game_id%
                                                \s
                                                 <b><color:#245a6a>Played:</b> %played%
                                                 <b><color:#245a6a>Finished:</b> %finished%
                                                \s
                                                 Results:
                                                """.replaceAll("%game_id%", String.valueOf(game.getId()))
                                                .replaceAll("%played%", String.valueOf(game.getPlayersJoined()))
                                                .replaceAll("%finished%", String.valueOf(game.getPlayersFinished())) + result)));
                            });
                        }))
                .executes((sender, args) -> {
                            Bukkit.getServer().dispatchCommand(sender, "glch help");
                        }
                )
                .register();
    }

    private void reload() {
        try {
            configurationFile.reload();
            gameManager.getChallengesFile().reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
