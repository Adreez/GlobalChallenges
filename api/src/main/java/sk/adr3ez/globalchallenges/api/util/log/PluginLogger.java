package sk.adr3ez.globalchallenges.api.util.log;

import org.jetbrains.annotations.Nullable;

public interface PluginLogger {

    void info(@Nullable String s);

    void warn(@Nullable String s);

    void severe(@Nullable String s);

}
