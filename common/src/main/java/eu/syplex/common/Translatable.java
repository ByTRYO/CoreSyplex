package eu.syplex.common;

import eu.syplex.common.exception.NotTranslatableException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Something that can be translated into something else.
 *
 * @param <T> The final type
 * @version 1.0.0
 * @since 1.2.0
 */
public interface Translatable<T> {

    /**
     * Translates the input of type {@code I} into an object of type {@code T}.
     *
     * @param input The input to translate
     * @param <I>   The type of the input
     * @return A translated object of type {@code T}
     * @throws NotTranslatableException Is thrown if the input cannot be translated. This is handled by the implementations rather than in an abstraction layer.
     */
    @Nullable <I> T translate(@NotNull I input) throws NotTranslatableException;

}
