package eu.syplex.common.item;

import eu.syplex.common.Builder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class ItemCreator implements Builder<ItemStack> {

    protected final ItemStack itemStack;
    protected final ItemMeta itemMeta;

    protected ItemCreator(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    protected ItemCreator(@NotNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    public abstract ItemCreator name(@NotNull TextComponent component);

    public abstract ItemCreator amount(int amount);

    public abstract ItemCreator lore(@NotNull TextComponent... components);

    public abstract ItemCreator removeLore();

    public abstract ItemCreator enchant(@NotNull Enchantment enchantment, int level);

    public abstract ItemCreator enchantUnsafe(@NotNull Enchantment enchantment, int level);

    @Override
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
