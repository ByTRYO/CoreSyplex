package eu.syplex.common.item;

import eu.syplex.common.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an implementation of an {@link Registry} to keep track of all created {@link NamedItem}s.
 *
 * @version 1.0.0
 * @since 1.3.0
 */
public class ItemRegistry implements Registry<NamedItem> {

	private final List<NamedItem> items = new ArrayList<>();

	private static ItemRegistry itemRegistry = null;

	/**
	 * Instantiates a new singleton {@link ItemRegistry}.
	 */
	private ItemRegistry() {
		itemRegistry = this;
	}

	/**
	 * Returns the singleton instance of the {@link ItemRegistry}.
	 *
	 * @return The singleton instance
	 */
	public static ItemRegistry itemRegistry() {
		if (itemRegistry == null) itemRegistry = new ItemRegistry();
		return itemRegistry;
	}

	/**
	 * Registers an {@link NamedItem} to keep track of it.
	 *
	 * @param toAdd The object to register
	 */
	@Override
	public void register(@NotNull NamedItem toAdd) {
		items.add(toAdd);
	}

	/**
	 * Unregisters an {@link NamedItem} if present.
	 *
	 * @param toRemove The object to unregister
	 */
	@Override
	public void unregister(@NotNull NamedItem toRemove) {
		items.remove(toRemove);
	}

	/**
	 * Returns a list of all currently registered items.
	 *
	 * @return The list
	 */
	public List<NamedItem> items() {
		return items;
	}
}
