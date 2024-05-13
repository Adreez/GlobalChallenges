package sk.adr3ez.globalchallenges.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GlobalChallengeStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public GlobalChallengeStartEvent() {
        //TODO - move to api and implement to the plugin
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
