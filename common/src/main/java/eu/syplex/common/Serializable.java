package eu.syplex.common;

import eu.syplex.common.exception.NotTranslatableException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Something that can serialized into something else.
 *
 * @param <T> The final type
 * @version 1.0.0
 * @since 1.2.0
 */
public interface Serializable<T> {

    /**
     * Serializes the input of type {@code I} into something of type {@code T}.
     *
     * @param input The input
     * @param <I>   The type of the input
     * @return A serialized object of type {@code I}
     * @throws NotTranslatableException Is thrown if the input cannot be serialized into something else. This is handled completely by the implementations of this interface.
     */
    @Nullable <I> T serialize(@NotNull I input) throws NotTranslatableException;

}
