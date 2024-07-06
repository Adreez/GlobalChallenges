package cz.darklabs.core.menu;

import cz.darklabs.core.api.RPGCoreProvider;
import cz.darklabs.core.menu.requirement.MenuRequirement;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class Menu<> {

    private final String title;
    private final int slots;
    private final List<MenuButton> buttons;
    private final Collection<MenuRequirement<?>> menuRequirements;
    private Player player;
    private Set<BukkitTask> schedulerTasks;

    public Menu(@NotNull String title,
                @NotNull Integer rows,
                @Nullable List<MenuButton> buttons,
                @Nullable Set<BukkitTask> schedulerTasks, Collection<MenuRequirement<?>> menuRequirements) {
        this.title = title;
        this.slots = rows * 9;
        this.buttons = buttons;
        this.schedulerTasks = schedulerTasks;
        this.menuRequirements = menuRequirements;
    }

    @Nullable
    public static Menu get(@NotNull Player player) {
        if (player.hasMetadata("DarkLabsMenu"))
            return (Menu) player.getMetadata("DarkLabsMenu").get(0).value();
        else
            return null;
    }

    public Inventory getInventory() {
        return this.player.getOpenInventory().getTopInventory();
    }

    private void handleClose() {
        //todo actions on close
        player.removeMetadata("DarkLabsMenu", RPGCoreProvider.get().getJavaPlugin());
        player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
        for (BukkitTask task : schedulerTasks) {
            task.cancel();
        }
    }

    public final void open(Player player) {
        this.player = player;

        //Save metadata
        player.setMetadata("DarkLabsMenu", new FixedMetadataValue(RPGCoreProvider.get().getJavaPlugin(), Menu.this));

        //todo Register buttons
        Inventory inv = Bukkit.createInventory(this.player, slots, Component.text(title));
        //todo setcontents for inventory
        // inv.setContents(List.of(new ItemStack(Material.ACACIA_FENCE)).toArray(new ItemStack[0]));

        player.openInventory(inv);

        //todo PostDisplayConsumer???
    }

    @Accessors(chain = true)
    @Setter
    public static class Builder {

        private final Collection<MenuRequirement<?>> openRequirements = new ArrayList<>();
        private String title = "New Menu";
        private int rows = 3;

        //TODO add menu options - debug numbers, show/hide players inventory, prevent close until...,
        // use bottom inventory, protect content, stop spamming (for each button?)

        public Builder() {
        }

        public void addRequirement(@NotNull MenuRequirement<?>... requirement) {
            openRequirements.addAll(List.of(requirement));
        }

        public void addButton(MenuButton... button) {

        }

        public void addSchedulerTask(BukkitTask... scheduler) {

        }

        public Menu build() {
            return new Menu(title, rows);
        }

    }


}
