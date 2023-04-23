package eu.syplex.scoreboard.exception;

/**
 * Represents an exception thrown when an entered line is longer than 16 chars.
 */
public class LineTooLongException extends Exception {

    /**
     * Instantiates a new {@link LineTooLongException} with the conflicting line and its length.
     *
     * @param string The conflicting line
     * @param length The lines length
     */
    public LineTooLongException(String string, int length) {
        super("The line '" + string + "' is longer than the maximum of 16 allowed chars! (" + length + " chars present)");
    }
}
