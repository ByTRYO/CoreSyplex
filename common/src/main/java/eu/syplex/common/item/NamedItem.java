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

    /**
     * Adds the item to the inventory.
     *
     * @param inventory The inventory
     */
    public abstract void addToInventory(@NotNull Inventory inventory);

    /**
     * Returns the customized item as a bukkit item.
     */
    public abstract @NotNull ItemStack toBukkitItem();

}
