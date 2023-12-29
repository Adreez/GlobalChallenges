package sk.adr3ez.globalchallenges;

import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class GCSpigotPlugin extends JavaPlugin implements GlobalChallenges {

    private BukkitAudiences adventure;

    //https://docs.advntr.dev/platform/bukkit.html
    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        // Initialize an audiences instance for the plugin
        this.adventure = BukkitAudiences.create(this);
        // then do any other initialization
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}
