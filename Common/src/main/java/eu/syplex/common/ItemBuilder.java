package eu.syplex.common;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemBuilder {
    ItemBuilder name(@NotNull String name);

    ItemBuilder enchant(@NotNull Enchantment enchantment, int level);

    ItemBuilder amount(int amount);

    ItemBuilder durability(short durability);

    ItemStack build();
}
