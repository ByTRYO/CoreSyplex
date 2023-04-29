package eu.syplex.common.item;

import eu.syplex.common.Beta;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract implementation for customized items. <br>
 * <b>Note, that this feature is marked as an {@link Beta} and may change in future releases.</b>
 *
 * @version 1.0.0
 * @since 1.3.0
 */
@Beta
public abstract class NamedItem {

	private final String identifier;

	/**
	 * Instantiates a new {@link NamedItem} and registers it into the {@link ItemRegistry}.
	 */
	public NamedItem(@NotNull String identifier) {
		this.identifier = identifier;
		ItemRegistry.itemRegistry().register(this);
	}

	/**
	 * Returns the unique identifier of this item. <br>
	 * This is commonly used to identify the item in event listeners like the {@link ItemListener}.
	 *
	 * @return the unique identifier
	 */
	public String identifier() {
		return identifier;
	}

	/**
	 * Adds the item to the inventory.
	 *
	 * @param inventory The inventory
	 */
	public abstract void addToInventory(@NotNull Inventory inventory);

	/**
	 * {@return The item as a bukkit item}
	 * Returns the customized item as a bukkit item.
	 */
	public abstract @NotNull ItemStack toBukkitItem();

}
