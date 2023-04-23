package eu.syplex.scoreboard.exception;

/**
 * Represents an exception thrown when an entered line is longer than 16 chars.
 */
public class LineTooLongException extends Exception {

    public LineTooLongException(String string, int length) {
        super("The line '" + string + "' is longer than the maximum of 16 allowed chars! (" + length + " chars present)");
    }
}
