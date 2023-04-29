package eu.syplex.item;

import eu.syplex.common.item.ItemCreator;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Represents an implementation of {@link ItemCreator} to create bukkit items with a non-specific {@link ItemMeta}.
 *
 * @version 1.0.0
 * @see ItemCreator
 * @since 1.0.0
 */
public class ItemBuilder extends ItemCreator {

	/**
	 * Instantiates this class using {@link ItemCreator#ItemCreator(Material)}.
	 *
	 * @param material The material
	 */
	public ItemBuilder(Material material) {
		super(material);
	}

	/**
	 * Instantiates this class using {@link ItemCreator#ItemCreator(Material, int)}.
	 *
	 * @param material The material
	 * @param amount   the amount
	 */
	public ItemBuilder(@NotNull Material material, int amount) {
		super(material, amount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator name(@NotNull TextComponent component) {
		itemMeta.displayName(component);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator amount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator lore(@NotNull TextComponent... components) {
		itemMeta.lore(Arrays.asList(components));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator removeLore() {
		itemStack.lore(null);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator enchant(@NotNull Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, false);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCreator enchantUnsafe(@NotNull Enchantment enchantment, int level) {
		itemMeta.addEnchant(enchantment, level, true);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T, Z> ItemCreator persistentData(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z data) {
		itemMeta.getPersistentDataContainer().set(key, type, data);
		return this;
	}
}
