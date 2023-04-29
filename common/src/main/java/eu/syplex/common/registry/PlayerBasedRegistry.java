package eu.syplex.common.registry;

import eu.syplex.common.Registry;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Registry} to keep track of something mapped to a {@link Player}.
 *
 * @param <T> The datatype of what should be mapped
 *
 * @version 1.0.0
 * @since 1.3.0
 */
public abstract class PlayerBasedRegistry<T> implements Registry<T> {

	/**
	 * Registers the object into registry and maps it to the player.
	 *
	 * @param player The player
	 * @param toAdd  The object to map
	 */
	public abstract void register(@NotNull Player player, @NotNull T toAdd);

	/**
	 * Unregisters the object from the registry if present.
	 *
	 * @param player   The player to which the object belongs
	 * @param toRemove The object to remove
	 */
	public abstract void unregister(@NotNull Player player, @NotNull T toRemove);

	/**
	 * {@inheritDoc}
	 *
	 * @param toAdd The object to register
	 *
	 * @throws UnsupportedOperationException When this method is called
	 */
	@Override
	public void register(@NotNull T toAdd) {
		throw new UnsupportedOperationException("To use a player based registry, please use player based registry methods!");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param toRemove The object to unregister
	 *
	 * @throws UnsupportedOperationException When this method is called
	 */
	@Override
	public void unregister(@NotNull T toRemove) {
		throw new UnsupportedOperationException("To use a player based registry, please use player based registry methods!");
	}
}
