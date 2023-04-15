package eu.syplex.common;

import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


public interface ItemBuilder {
    ItemBuilder name(@NotNull Component name);

    ItemBuilder lore(@NotNull Component... lore);

    ItemBuilder enchant(@NotNull Enchantment enchantment, int level);

    ItemBuilder enchantUnsafe(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction);

    ItemBuilder amount(int amount);

    ItemBuilder durability(short durability);

    ItemStack build();
}
