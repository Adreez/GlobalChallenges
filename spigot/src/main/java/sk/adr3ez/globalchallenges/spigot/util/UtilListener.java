package sk.adr3ez.globalchallenges.spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import sk.adr3ez.globalchallenges.api.GlobalChallengesProvider;
import sk.adr3ez.globalchallenges.api.database.entity.DBPlayer;
import sk.adr3ez.globalchallenges.core.database.dao.PlayerDAO;

//Utility listeners for spigot can be found here
public class UtilListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    void join(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(GlobalChallengesProvider.get().getJavaPlugin(), () -> {
            DBPlayer DBPlayer = new DBPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
            PlayerDAO.saveOrUpdate(DBPlayer);
        });
    }

}
