package cz.darklabs.core.menu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuButton {

    @Getter
    private ItemStack itemStack = null;

    public MenuButton(@Nullable ItemStack itemStack) {
        this.itemStack = (itemStack == null ? new ItemStack(Material.BARRIER) : itemStack);
    }

    public interface ClickAction {
        void onClick(@NotNull Player player, @NotNull InventoryClickEvent event, @NotNull Menu menu);
    }

    @NoArgsConstructor
    public static class Builder {

        private ItemStack itemStack = null;

        public Builder material(@NotNull Material material) {
            this.itemStack = new ItemStack(material);
            return this;
        }

        public Builder material(@NotNull ItemStack material) {
            this.itemStack = material;
            return this;
        }
        /*public Builder material(@NotNull ItemBuilder material) {
            this.itemStack = new ItemStack(material);
            return this;
        }*/

        public MenuButton build() {
            return new MenuButton(null);
        }


    }

}
