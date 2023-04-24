package eu.syplex.common.exception;

/**
 * Represents an exception thrown if something cannot be translated into something else.
 */
public class NotTranslatableException extends Exception {

    /**
     * Instantiates a new {@link NotTranslatableException} with the conflicting object and the type the result should be type of.
     *
     * @param obj The conflicting object
     * @param should  The type the result should be type of
     */
    public NotTranslatableException(Object obj, Class<?> should) {
        super("The object of type " + obj.getClass().getSimpleName() + " cannot be translated into " + should.getSimpleName() + "!");
    }
}
