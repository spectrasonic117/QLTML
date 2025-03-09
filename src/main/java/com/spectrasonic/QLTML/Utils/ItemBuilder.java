package com.spectrasonic.QLTML.Utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;
    private final Set<ItemFlag> flags = new HashSet<>();

    public static ItemBuilder setMaterial(String materialName) {
        Material material = Material.matchMaterial(materialName.toUpperCase());
        if (material == null) throw new IllegalArgumentException("Invalid material: " + materialName);
        return new ItemBuilder(material);
    }

    private ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.displayName(MiniMessage.miniMessage().deserialize(name));
        return this;
    }

    public ItemBuilder setLore(String... loreLines) {
        meta.lore(java.util.Arrays.stream(loreLines)
                .map(MiniMessage.miniMessage()::deserialize)
                .toList());
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        meta.setCustomModelData(customModelData);
        return this;
    }

    public ItemBuilder addEnchantment(String enchantmentName, int level) {
        String normalized = enchantmentName.toUpperCase().toLowerCase();
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(normalized));
        if (enchantment == null) {
            throw new IllegalArgumentException("Invalid enchantment name: " + enchantmentName);
        }
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setFlag(String flagName) {
        try {
            ItemFlag flag = ItemFlag.valueOf(flagName.toUpperCase().replace(" ", "_"));
            flags.add(flag);
            return this;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid flag: " + flagName);
        }
    }

    public ItemStack build() {
        meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        item.setItemMeta(meta);
        return item;
    }
}