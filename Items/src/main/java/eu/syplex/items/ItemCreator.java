package eu.syplex.items;

import eu.syplex.common.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemCreator implements ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemCreator(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    @Override
    public ItemBuilder name(@NotNull String name) {
        return null;
    }

    @Override
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        return null;
    }

    @Override
    public ItemBuilder amount(int amount) {
        return null;
    }

    @Override
    public ItemBuilder durability(short durability) {
        return null;
    }

    @Override
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
