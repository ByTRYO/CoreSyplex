package eu.syplex.items;

import eu.syplex.common.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemCreator implements ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemCreator(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    @Override
    public ItemBuilder name(@NotNull Component name) {
        itemMeta.displayName(name);
        return this;
    }

    @Override
    public ItemBuilder lore(@NotNull Component... lore) {
        itemMeta.lore(Arrays.asList(lore));
        return this;
    }

    @Override
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    @Override
    public ItemBuilder enchantUnsafe(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    @Override
    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemBuilder durability(short durability) {
        ((Damageable) itemMeta).setDamage(durability);
        return this;
    }

    @Override
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
