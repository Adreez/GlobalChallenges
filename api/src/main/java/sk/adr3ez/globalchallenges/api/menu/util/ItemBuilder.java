package cz.darklabs.core.menu.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class ItemBuilder {

    private final ItemStack item;

    private ItemBuilder(final Material itemType, String name, String... lore) {
        item = new ItemStack(itemType);
        name(name);
        setLore(new ArrayList<>(List.of(lore)));
    }

    private ItemBuilder(final Material itemType) {
        item = new ItemStack(itemType);
    }

    private ItemBuilder(final ItemStack itemStack) {
        item = itemStack;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public ItemBuilder type(final Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(final String name) {
        if (name == null) return this;
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            im.setDisplayName(name);
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        if (lore == null) return this;
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            im.setLore(lore);
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder setLore(final String lore) {
        if (lore == null) return this;
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            //im.setLore(Collections.singletonList(ChatUtils.format(lore)));
            im.setLore(Collections.singletonList(lore));
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder addLore(final String... strings) {
        if (strings == null) return this;
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            if (im.lore() != null) {
                ArrayList<String> lore = new ArrayList<>(im.getLore());
                lore.addAll(List.of(strings));
                im.setLore(lore);
            } else {
                im.setLore(List.of(strings));
            }
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder hideAttributes() {
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder customModelData(final int modelData) {
        if (item.getType() != Material.AIR) {
            ItemMeta im = item.getItemMeta();
            im.setCustomModelData(modelData);
            item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder setOwningPlayer(String skullValue) {
        /* For spigot
        if (item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", skullValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                Bukkit.getLogger().warning("Failed to set base64 skull value!");
            }
            item.setItemMeta(meta);
        }
        return this;*/

        // For paper
        if (item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", skullValue));
            meta.setPlayerProfile(profile);

            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setOwningPlayer(OfflinePlayer p) {
        if (item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(p);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack make() {
        return item;
    }

}
