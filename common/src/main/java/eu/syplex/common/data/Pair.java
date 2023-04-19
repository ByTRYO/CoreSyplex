package eu.syplex.common.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a generic pair of data.
 *
 * @param <A> The first datatype stored
 * @param <B> The second datatype stored
 * @version 1.0.0
 * @since 1.0.0
 */
public class Pair<A, B> {

    /**
     * The first peace of data
     */
    private final A first;

    /**
     * The second peace of data
     */
    private final B second;

    /**
     * Instantiates a new pair of data with both peaces of data.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     */
    protected Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new pair of data using the constructor of {@link Pair}.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     * @return the pair
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static <A, B> @NotNull Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    /**
     * Returns the first peace of data.
     *
     * @return The first peace of data
     */
    public A first() {
        return first;
    }

    /**
     * Returns the second peace of data.
     *
     * @return The second peace of data
     */
    public B second() {
        return second;
    }

}
