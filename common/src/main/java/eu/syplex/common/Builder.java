package eu.syplex.common;

/**
 * This is an abstraction of every single builder in this entire project.
 *
 * @param <T> The datatype of the result that will be built
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Builder<T> {

    /**
     * Builds the object to the defined datatype and returns it.
     *
     * @return The built object
     */
    T build();

}
