package cz.darklabs.core.menu.requirement.list;

import cz.darklabs.core.menu.requirement.MenuRequirement;
import org.bukkit.entity.Player;

public final class PermissionRequirement implements MenuRequirement<String> {
    @Override
    public boolean isMet(Player player, String object) {
        return player.hasPermission(object);
    }
}
