package eu.syplex.common.item;

import eu.syplex.common.Builder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract implementation of the {@link Builder} interface to create bukkit items.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ItemCreator implements Builder<ItemStack> {

    /**
     * The item that will be created
     */
    protected final ItemStack itemStack;

    /**
     * The meta of the item
     */
    protected final ItemMeta itemMeta;

    /**
     * Instantiates a new {@link ItemCreator} object. This will create a new {@link ItemStack} with the given {@link Material}, the amount of one
     * and initializes the {@link ItemMeta} from it to be able to modify it later.
     *
     * @param material The material
     */
    protected ItemCreator(@NotNull Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Instantiates a new {@link ItemCreator} object. This will create a new {@link ItemStack} with the given {@link Material} and amount.
     * After that the {@link ItemMeta} gets initialized to be able to modify it later.
     *
     * @param material The material
     * @param amount   The amount
     */
    protected ItemCreator(@NotNull Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Sets the displayed name of the item using a {@link TextComponent}.
     *
     * @param component The component
     * @return This current object instance
     */
    public abstract ItemCreator name(@NotNull TextComponent component);

    /**
     * Defines, how many items the stack will have. Note, that the maximum is 64 because of dump implementations from Mojang.
     *
     * @param amount The amount
     * @return This current object instance
     */
    public abstract ItemCreator amount(int amount);

    /**
     * Sets the displayed text in the item's information box. Each {@link TextComponent} represents a single line. Note, that an empty line is created by calling {@code Component.empty()}.
     *
     * @param components The components
     * @return This current object instance
     */
    public abstract ItemCreator lore(@NotNull TextComponent... components);

    /**
     * Removes the entire displayed text in the items' information box.
     *
     * @return This current object instance
     */
    public abstract ItemCreator removeLore();

    /**
     * Enchants the item with a specific {@link Enchantment} and level. This implementation only allows the default values that are currently implemented in the game.
     * Note: To break the games' laws and enchant with a higher level than the maximum defaults, please use {@link #enchantUnsafe(Enchantment, int)}
     *
     * @param enchantment The enchantment
     * @param level       The level
     * @return This current object instance
     */
    public abstract ItemCreator enchant(@NotNull Enchantment enchantment, int level);

    /**
     * Enchants the item with a specific {@link Enchantment} and level. This implementation allows you to ignore level restrictions.
     *
     * @param enchantment The enchantment
     * @param level       The level
     * @return This current object instance
     */
    public abstract ItemCreator enchantUnsafe(@NotNull Enchantment enchantment, int level);

    /**
     * {@inheritDoc}
     *
     * @see Builder
     */
    @Override
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
