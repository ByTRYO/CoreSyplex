package eu.syplex.common.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represent an advanced implementation of {@link Triplet} containing a fourth peace of data.
 *
 * @param <A> The first datatype stored
 * @param <B> The second datatype stored
 * @param <C> The third datatype stored
 * @param <D> The fourth datatype stored
 * @version 1.0.0
 * @see Triplet
 * @since 1.0.0
 */
public class Quartet<A, B, C, D> extends Triplet<A, B, C> {

    /**
     * The fourth peace of data
     */
    private final D fourth;

    /**
     * Instantiates a new quartet of data with all four peaces of data using {@link Triplet}.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     * @param third  The third peace of data
     * @param fourth The fourth peace of data
     */
    private Quartet(A first, B second, C third, D fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    /**
     * Creates a new quartet of data using the constructor of {@link Quartet}.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     * @param third  The third peace of data
     * @param fourth The fourth peace of data
     * @return the quartet
     */
    @Contract("_, _, _, _ -> new")
    public static <A, B, C, D> @NotNull Quartet<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quartet<>(first, second, third, fourth);
    }

    /**
     * Returns the fourth peace of data
     *
     * @return The fourth peace of data
     */
    public D fourth() {
        return fourth;
    }

}
