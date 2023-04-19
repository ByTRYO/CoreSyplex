package eu.syplex.common.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an advanced implementation of {@link Pair} containing a third peace of data.
 *
 * @param <A> The first datatype stored
 * @param <B> The second datatype stored
 * @param <C> The third datatype stored
 * @version 1.0.0
 * @see Pair
 * @since 1.0.0
 */
public class Triplet<A, B, C> extends Pair<A, B> {

    /**
     * The third peace of data
     */
    private final C third;

    /**
     * Instantiates a new triplet of data with all three peaces of data using {@link Pair}.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     * @param third  The third peace of data
     */
    protected Triplet(A first, B second, C third) {
        super(first, second);
        this.third = third;
    }


    /**
     * Creates a new triplet of data using the constructor of {@link Triplet}.
     *
     * @param first  The first peace of data
     * @param second The second peace of data
     * @param third  The third peace of data
     * @return the triplet
     */
    @Contract("_, _, _ -> new")
    public static <A, B, C> @NotNull Triplet<A, B, C> of(A first, B second, C third) {
        return new Triplet<>(first, second, third);
    }

    /**
     * Returns the third peace of data.
     *
     * @return The third peace of data
     */
    public C third() {
        return third;
    }
}
