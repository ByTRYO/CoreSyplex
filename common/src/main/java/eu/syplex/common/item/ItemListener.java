package eu.syplex.common.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a concrete implementation for running interactions with a {@link NamedItem}. <br>
 * In order to be able to use named items, you have to implement your own checks by inherit this class
 * and registering your implementation as a listener in your environment.
 *
 * @version 1.0.0
 * @since 1.3.0
 */
public abstract class ItemListener implements Listener {

	private final NamespacedKey key;

	/**
	 * Instantiates a new {@link ItemListener} with the {@link NamespacedKey} to detect custom named items.
	 *
	 * @param key The key
	 */
	public ItemListener(@NotNull NamespacedKey key) {
		this.key = key;
	}

	/**
	 * Represents the logic behind running the interactions. <br>
	 * <b>Do not call this method. This in an implementation of an {@link Listener}!</b>
	 *
	 * @param event The event that is called
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();

		try {
			if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;

			final ItemStack itemStack = player.getInventory().getItemInMainHand();
			event.setCancelled(checkCancellation(player, itemStack.getType()));

			if (!itemStack.hasItemMeta()) return;
			final PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();

			if (!container.has(key)) return;
			final String nbt = container.get(key, PersistentDataType.STRING);

			for (final NamedItem item : ItemRegistry.itemRegistry().items()) {
				if (!item.identifier().equals(nbt)) continue;
				if (!(item instanceof final Intractable intractable)) continue;

				intractable.onInteract(event);
				return;
			}


		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Abstract implementation for checking if the {@link #onInteract(PlayerInteractEvent)} should be canceled or not.
	 * This determines if the logic behind running interactions should be performed or not. <br><br>
	 * <b>Implement this method only and do not under any circumstances call this method by yourself!</b>
	 *
	 * @param player  The player that interacts
	 * @param toCheck The material to check.
	 *
	 * @return {@code true} if the event should be canceled. Otherwise, {@code false}.
	 */
	public abstract boolean checkCancellation(@NotNull Player player, @NotNull Material toCheck);

}
