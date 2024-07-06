package cz.darklabs.core.menu.requirement;

import org.bukkit.entity.Player;

public interface MenuRequirement<T> {

    boolean isMet(Player player, T object);

}
