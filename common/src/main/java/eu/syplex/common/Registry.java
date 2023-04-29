package eu.syplex.common;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an abstract implementation of all registries.
 *
 * @param <T> The datatype which is stored in the registry
 *
 * @version 1.0.0
 * @since 1.3.0
 */
public interface Registry<T> {

	/**
	 * Registers the object into the registry.
	 *
	 * @param toAdd The object to register
	 */
	void register(@NotNull T toAdd);

	/**
	 * Unregisters the object from the registry if present.
	 *
	 * @param toRemove The object to unregister
	 */
	void unregister(@NotNull T toRemove);

}
