package sk.adr3ez.globalchallenges.spigot.util;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import sk.adr3ez.globalchallenges.api.GlobalChallenges;

public class BlockListener implements Listener {

    @NotNull
    private final GlobalChallenges plugin;

    public BlockListener(GlobalChallenges plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void place(BlockPlaceEvent event) {
        Location loc = event.getBlock().getLocation();

        write(event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void destroy(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        remove(event.getBlock().getLocation());
    }


    private void write(Location location) {

        int loc = location.hashCode();

        location.getChunk().getPersistentDataContainer().set(new NamespacedKey(plugin.getJavaPlugin(), Integer.toString(loc, 16)), PersistentDataType.INTEGER, 1);
        //DEBUG plugin.getPluginLogger().warn("Keys: " + location.getChunk().getPersistentDataContainer().getKeys());
    }

    private void remove(Location location) {

        int loc = location.hashCode();
        location.getChunk().getPersistentDataContainer().remove(new NamespacedKey(plugin.getJavaPlugin(), Integer.toString(loc, 16)));

    }
}
